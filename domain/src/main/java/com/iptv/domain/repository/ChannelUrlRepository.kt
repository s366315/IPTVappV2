package com.iptv.domain.repository

import com.iptv.domain.entities.ChannelUrl
import com.iptv.domain.entities.Result

interface ChannelUrlRepository {
    suspend fun getUrl(
        channelId: String
    ): Result<ChannelUrl>
}