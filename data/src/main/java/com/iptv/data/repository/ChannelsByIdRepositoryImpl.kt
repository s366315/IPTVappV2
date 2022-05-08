package com.iptv.data.repository

import com.iptv.data.api.ApiService
import com.iptv.data.converters.toChannel
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.Channel
import com.iptv.domain.entities.Result
import com.iptv.domain.repository.ChannelsByIdRepository
import javax.inject.Inject

class ChannelsByIdRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferences: Preferences,
) : ChannelsByIdRepository {
    override suspend fun channelListById(cids: List<String>): Result<List<Channel>> {
        val data = apiService.channelsById(
            ssid = preferences.sid,
            cids = cids.joinToString(separator = ",")
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