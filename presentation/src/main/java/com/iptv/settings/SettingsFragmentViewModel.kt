package com.iptv.settings

import androidx.lifecycle.viewModelScope
import com.iptv.base.BaseViewModel
import com.iptv.data.converters.toLoginDetails
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.LoginDetails
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.settings.SettingsEnum
import com.iptv.domain.interactor.settings.SettingsUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class SettingsFragmentViewModel : BaseViewModel() {
    abstract val profileState: SharedFlow<LoginDetails>
    abstract val settingsSaveState: SharedFlow<String>

    abstract fun selectBuffer(buffer: Int)
    abstract fun saveSettings()
}

class SettingsFragmentViewModelImpl @Inject constructor(
    private val preferences: Preferences,
    private val settingsUseCase: SettingsUseCase
) : SettingsFragmentViewModel() {

    override val profileState = MutableSharedFlow<LoginDetails>(replay = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val settingsSaveState = MutableSharedFlow<String>()

    private var loginDetails: LoginDetails? = null
    private var buffer = 0

    override fun selectBuffer(buffer: Int) {
        this.buffer = buffer
    }

    override fun saveSettings() {
        if (buffer != loginDetails?.settings?.httpCaching?.value) {
            preferences.buffer = buffer
            viewModelScope.launch {
                setLoading(true)

                val result = settingsUseCase.createObservable(
                    SettingsUseCase.Params(
                        SettingsEnum.HTTP_CACHING,
                        buffer.toString()
                    )
                )

                when (result) {
                    is Result.Success -> {
                        settingsSaveState.emit(result.data)
                    }
                    is Result.Error -> {
                        setError(result.message)
                    }
                }
                setLoading(false)
            }
        }
    }

    init {
        viewModelScope.launch {
            loginDetails = preferences.toLoginDetails()
            profileState.emit(
                preferences.toLoginDetails()
            )
        }
    }
}