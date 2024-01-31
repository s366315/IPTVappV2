package com.iptv.domain.interactor.signin

import com.iptv.domain.entities.LoginDetails
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.UseCase
import com.iptv.domain.repository.LoginRepository

class LoginUseCase(
    private val authRepository: LoginRepository
) : UseCase<LoginUseCase.Params, LoginDetails>() {

    data class Params(
        val login: String,
        val password: String,
        val softId: String,
        val cliSerial: String
    )

    override suspend fun createObservable(
        params: Params?,
        onSuccess: suspend (Result.Success<LoginDetails>) -> Unit,
        onError: suspend (Result.Error) -> Unit
    ) {
        params?.let {
            val result = authRepository.login(
                login = params.login,
                password = params.password,
                softId = params.softId,
                cliSerial = params.cliSerial
            )

            when (result) {
                is Result.Success -> {
                    onSuccess(result)
                }
                is Result.Error -> {
                    onError(result)
                }
                else -> {}
            }
        } ?: throw IllegalStateException("LoginUseCase.Params must not be null")
    }
}