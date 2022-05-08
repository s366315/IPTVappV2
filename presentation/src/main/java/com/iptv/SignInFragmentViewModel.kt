package com.iptv

import androidx.lifecycle.viewModelScope
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.signin.LoginUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInFragmentViewModel @Inject constructor(
    private val preferences: Preferences,
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _signedInState = MutableSharedFlow<Boolean>()
    val signedInState: SharedFlow<Boolean> = _signedInState

    init {
        viewModelScope.launch {

            if (preferences.login.isEmpty()) { //показываем окно логина, если логин не сохранён
                _signedInState.emit(false)
            } else {
                doSignIn(
                    login = preferences.login,
                    password = preferences.password
                )
            }
        }
    }

    fun doSignIn(login: String, password: String) {
        viewModelScope.launch {
            setLoading(true)

            val result = loginUseCase.params(
                LoginUseCase.Params(
                    login = login,
                    password = password,
                    softId = "android",
                    cliSerial = "adrgw23q232ew"
                )
            ).subscribe()

            when (result) {
                is Result.Success -> {
                    _signedInState.emit(true)
                }
                is Result.Error -> {
                    setError(result.message)
                }
            }

            setLoading(false)
        }
    }
}