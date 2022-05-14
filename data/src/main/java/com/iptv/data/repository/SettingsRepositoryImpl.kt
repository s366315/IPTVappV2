package com.iptv.data.repository

import com.iptv.data.api.ApiService
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.settings.SettingsEnum
import com.iptv.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferences: Preferences
) : SettingsRepository {

    override suspend fun setSettings(variant: SettingsEnum, value: String): Result<String> {
        val data = apiService.setSettings(
            ssid = preferences.sid,
            variant = variant(),
            value = value
        )

        data.error?.let {
            return Result.Error(it.code.toInt(), it.message)
        }

        return Result.Success(
            data = data.message.text
        )
    }
}