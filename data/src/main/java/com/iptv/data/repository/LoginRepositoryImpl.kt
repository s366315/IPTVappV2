package com.iptv.data.repository

import com.iptv.data.api.ApiService
import com.iptv.data.preferences.Preferences
import com.iptv.data.response.toJString
import com.iptv.domain.entities.*
import com.iptv.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferences: Preferences
) : LoginRepository {

    private companion object {
        const val SETTINGS_ALL = "all"
    }

    override suspend fun login(
        login: String,
        password: String,
        softId: String,
        cliSerial: String
    ): Result<LoginDetails> {
        val data = apiService.login(
            login = login,
            password = password,
            softId = softId,
            cliSerial = cliSerial,
            settings = SETTINGS_ALL
        )

        data.error?.let {
            preferences.clean()
            return Result.Error(it.code.toInt(), it.message)
        }

        preferences.apply {
            sid = data.sid
            sidName = data.sidName ?: ""
            this.login = data.account?.login ?: ""
            this.password = password
            packetName = data.account?.packetName ?: ""
            packetId = data.account?.packetId ?: ""
            packetExpire = data.account?.packetExpire ?: 0
            vod = data.services?.vod == 1
            archive = data.services?.archive == 1
            buffer = data.settings?.httpCaching?.value?.toInt() ?: 0
            bufferList = data.settings?.httpCaching?.list?.toJString() ?: ""
        }

        return Result.Success(
            LoginDetails(
                sid = data.sid ?: "",
                sidName = data.sidName ?: "",
                account = Account(
                    login = data.account?.login ?: "",
                    packetName = data.account?.packetName ?: "",
                    packetId = data.account?.packetId ?: "",
                    packetExpire = data.account?.packetExpire ?: 0
                ),
                services = Services(
                    vod = data.services?.vod == 1,
                    archive = data.services?.archive == 1
                ),
                settings = Settings(
                    HttpCaching(
                        value = data.settings?.httpCaching?.value?.toInt() ?: 0,
                        list = data.settings?.httpCaching?.list ?: emptyList()
                    )
                )
            )
        )
    }
}