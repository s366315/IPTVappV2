package com.iptv.domain.repository

import com.iptv.domain.entities.LoginDetails
import com.iptv.domain.entities.Result

interface LoginRepository {
    suspend fun login(
        login: String,
        password: String,
        softId: String,
        cliSerial: String
    ): Result<LoginDetails>
}