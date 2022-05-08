package com.iptv.data.repository

import com.iptv.data.api.ApiService
import com.iptv.data.converters.toChannel
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.Channel
import com.iptv.domain.entities.Result
import com.iptv.domain.repository.ChannelListRepository
import javax.inject.Inject

class ChannelListRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferences: Preferences,
) : ChannelListRepository {

    override suspend fun channelList(
        showProtected: String?,
        protectedCode: String?
    ): Result<List<Channel>> {
        val data = apiService.channelList(
            showProtected = showProtected, protectedCode = protectedCode, ssid = preferences.sid
        )

        data.error?.let {
            return Result.Error(it.code.toInt(), it.message)
        }

        return Result.Success(
            data.groups?.flatMap { group ->
                group.channels.map {
                    it.toChannel(group)
                }
            } ?: emptyList()
        )
    }
}