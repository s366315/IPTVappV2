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

    override suspend fun createObservable(params: Params?): Result<String> {
        params?.let {
            return settingsRepository.setSettings(
                variant = params.variant,
                value = params.value
            )
        } ?: throw IllegalStateException("SettingsUseCase.Params must not be null")
    }
}