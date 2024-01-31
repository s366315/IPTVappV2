package com.iptv.domain.interactor.settings

import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.UseCase
import com.iptv.domain.repository.SettingsRepository

class SettingsUseCase(
    private val settingsRepository: SettingsRepository
) : UseCase<SettingsUseCase.Params, String>() {

    data class Params(
        val variant: SettingsEnum,
        val value: String
    )

    override suspend fun createObservable(
        params: Params?,
        onSuccess: suspend (Result.Success<String>) -> Unit,
        onError: suspend (Result.Error) -> Unit
    ) {
        params?.let {
            val result = settingsRepository.setSettings(
                variant = params.variant,
                value = params.value
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
        } ?: throw IllegalStateException("SettingsUseCase.Params must not be null")
    }
}