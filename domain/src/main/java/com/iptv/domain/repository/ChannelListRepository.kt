package com.iptv.domain.repository

import com.iptv.domain.entities.Channel
import com.iptv.domain.entities.Result

interface ChannelListRepository {
    suspend fun channelList(
        showProtected: String?,
        protectedCode: String?
    ): Result<List<Channel>>
}