package com.iptv.domain.repository

import com.iptv.domain.entities.*
import com.iptv.domain.interactor.settings.SettingsEnum

interface SettingsRepository {
    suspend fun setSettings(variant: SettingsEnum, value: String): Result<String>
}