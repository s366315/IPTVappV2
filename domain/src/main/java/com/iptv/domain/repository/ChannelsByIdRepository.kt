package com.iptv.domain.repository

import com.iptv.domain.entities.Channel
import com.iptv.domain.entities.Result

interface ChannelsByIdRepository {
    suspend fun channelListById(
        cids: List<String>
    ): Result<List<Channel>>
}