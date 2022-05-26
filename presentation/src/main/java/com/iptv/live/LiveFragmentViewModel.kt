package com.iptv.live

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.iptv.base.BaseViewModel
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.Channel
import com.iptv.domain.interactor.channelList.ChannelListUseCase
import com.iptv.domain.interactor.channelUrl.ChannelUrlUseCase
import com.iptv.domain.interactor.channelsById.ChannelsByIdUseCase
import com.iptv.domain.interactor.signin.LoginUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class LiveFragmentViewModel : BaseViewModel() {
    abstract val channelListState: SharedFlow<List<Channel>>
    abstract val channelUrlState: SharedFlow<String>
    abstract val onRootClickState: SharedFlow<Unit>
    abstract val onBtnShowChannelsClickState: SharedFlow<Unit>
    abstract val onBtnSettingsClickState: SharedFlow<Unit>
    abstract val bottomSheetState: StateFlow<Int>
    abstract val channelState: SharedFlow<Channel>

    abstract var onRootClick: (Unit) -> Unit
    abstract var onBtnShowChannelsClick: (Unit) -> Unit
    abstract var onBtnSettingsClick: (Unit) -> Unit
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

    override val channelListState = MutableSharedFlow<List<Channel>>(
        replay = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val channelUrlState = MutableSharedFlow<String>()
    override val onRootClickState = MutableSharedFlow<Unit>()
    override val onBtnShowChannelsClickState = MutableSharedFlow<Unit>()
    override val onBtnSettingsClickState = MutableSharedFlow<Unit>()
    override val bottomSheetState = MutableStateFlow(BottomSheetBehavior.STATE_COLLAPSED)
    override val channelState = MutableSharedFlow<Channel>()

    private val timer = ChannelsTimer()
    private val needToUpdateChannels = MutableStateFlow<List<String>>(emptyList())

    override fun onCleared() {
        timer.destroy()
        super.onCleared()
    }

    init {

        viewModelScope.launch {
            getChannels()
        }

        viewModelScope.launch {
            needToUpdateChannels.collectLatest {
                if (it.isNotEmpty()) {
                    println("channels $it is need to update")
                    getChannels()
                }
            }
        }
    }

    private suspend fun getChannels() {
        setLoading(true)

        channelListUseCase.createObservable(
            onSuccess = {
                val channelsList = it.data
                timer.onTick {
                    needToUpdateChannels.tryEmit(
                        channelsList.filter { it.isVideo }
                            .filter { it.epgEnd <= System.currentTimeMillis().div(1000) }
                            .map { it.id }
                    )
                }
                channelListState.emit(it.data)
//                channelState.emit(channelsList.first { channelState.asSharedFlow() })
            }, onError = {
                setError(it.message)
            }
        )

        setLoading(false)
    }

    private suspend fun updateChannels(list: List<String>) {
        channelsByIdUseCase.createObservable(ChannelsByIdUseCase.Params(list),
            onSuccess = {
                channelListState.emit(it.data)
            }, onError = {
                setError(it.message)
            }
        )
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

    override var onBtnSettingsClick: (Unit) -> Unit = {
        viewModelScope.launch {
            onBtnSettingsClickState.emit(Unit)
        }
    }

    override var onChannelClick: (Channel) -> Unit = {
        viewModelScope.launch {
            channelState.emit(it)
        }
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
                ),
                onSuccess = {
                    channelUrlState.emit(it.data.url)
                },
                onError = {
                    setError(it.message)
                }
            )
        }
    }
}

class ChannelsTimer {
    val timer: CountDownTimer
    var onTick: (() -> Unit)? = null

    init {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick?.invoke()
            }

            override fun onFinish() {

            }
        }
    }

    fun onTick(onTick: () -> Unit) {
        this.onTick = onTick
        timer.start()
    }

    fun destroy() {
        timer.cancel()
    }
}