package com.iptv.data.repository

import com.iptv.data.api.ApiService
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.ChannelUrl
import com.iptv.domain.entities.Result
import com.iptv.domain.repository.ChannelUrlRepository
import javax.inject.Inject

class ChannelUrlRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferences: Preferences
) : ChannelUrlRepository {

    override suspend fun getUrl(channelId: String): Result<ChannelUrl> {
        val response = apiService.getUrl(
            ssid = preferences.sid,
            channelId = channelId
        )

        var url = response.url ?: ""
        url = url.split(" ")[0]
        url = url.replace("http/ts", "http", true)

        response.error?.let {
            return Result.Error(it.code.toInt(), it.message)
        }

        return Result.Success(ChannelUrl(url = url))
    }
}