package com.iptv.live

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.iptv.BaseViewModel
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.Channel
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.channelList.ChannelListUseCase
import com.iptv.domain.interactor.channelUrl.ChannelUrlUseCase
import com.iptv.domain.interactor.channelsById.ChannelsByIdUseCase
import com.iptv.domain.interactor.signin.LoginUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.nio.BufferOverflowException
import javax.inject.Inject

abstract class LiveFragmentViewModel : BaseViewModel() {
    abstract val channelListState: SharedFlow<List<Channel>>
    abstract val channelUrlState: SharedFlow<String>
    abstract val onRootClickState: SharedFlow<Unit>
    abstract val onBtnShowChannelsClickState: SharedFlow<Unit>
    abstract val bottomSheetState: StateFlow<Int>

    abstract var onRootClick: (Unit) -> Unit
    abstract var onBtnShowChannelsClick: (Unit) -> Unit
    abstract var onChannelClick: (Channel) -> Unit
    abstract var onSheetState: (Int) -> Unit
}

class LiveFragmentViewModelImpl @Inject constructor(
    private val preferences: Preferences,
    private val loginUseCase: LoginUseCase,
    private val channelListUseCase: ChannelListUseCase,
    private val channelUrlUseCase: ChannelUrlUseCase,
    private val channelsByIdUseCase: ChannelsByIdUseCase
) : LiveFragmentViewModel() {

    override val channelListState = MutableSharedFlow<List<Channel>>(replay = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val channelUrlState = MutableSharedFlow<String>()
    override val onRootClickState = MutableSharedFlow<Unit>()
    override val onBtnShowChannelsClickState = MutableSharedFlow<Unit>()
    override val bottomSheetState = MutableStateFlow(BottomSheetBehavior.STATE_COLLAPSED)

    val timer = ChannelsTimer()
    val needToUpdateChannels = MutableStateFlow<List<String>>(emptyList())

    override fun onCleared() {
        timer.destroy()
        super.onCleared()
    }

    init {

        viewModelScope.launch {
            setLoading(true)

            val channels = channelListUseCase.subscribe()

            when (channels) {
                is Result.Success -> {
                    val channelsList = channels.data
                    timer.emitList {
                        needToUpdateChannels.tryEmit(
                            channelsList.filter { it.isVideo }.filter { it.epgEnd <= System.currentTimeMillis().div(1000) }.map { it.id }
                        )
//                        channelListState.tryEmit(channels.data)
                    }
                    channelListState.tryEmit(channels.data)
//                    channelListState.value = channels.data
                }
                is Result.Error -> {
                    setError(channels.message)
                }
            }

            setLoading(false)
        }

        viewModelScope.launch {
            needToUpdateChannels.collectLatest {
                if (it.isNotEmpty()) {
                    println("channels $it is need to update")
                }
            }
        }
    }

    override var onRootClick: (Unit) -> Unit = {
        viewModelScope.launch {
            onRootClickState.emit(Unit)
        }
    }

    override var onBtnShowChannelsClick: (Unit) -> Unit = {
        viewModelScope.launch {
            onBtnShowChannelsClickState.emit(Unit)
        }
    }

    override var onChannelClick: (Channel) -> Unit = {
        getChannelUrl(it.id)
    }

    override var onSheetState: (Int) -> Unit = {
        bottomSheetState.value = it
    }

    private fun getChannelUrl(channelId: String) {
        viewModelScope.launch {
            channelUrlUseCase.createObservable(
                ChannelUrlUseCase.Params(
                    channelId = channelId
                )
            ).also {
                when (it) {
                    is Result.Success -> {
                        channelUrlState.emit(it.data.url)
                    }
                    is Result.Error -> {
                        setError(it.message)
                    }
                }
            }
        }
    }
}

class ChannelsTimer {
    val timer: CountDownTimer
    var onTick: (() -> Unit)? = null

    init {
        timer = object: CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick?.invoke()
            }

            override fun onFinish() {

            }
        }
    }

    fun emitList(onTick: () -> Unit) {
        this.onTick = onTick
        timer.start()
    }

    fun destroy() {
        timer.cancel()
    }
}