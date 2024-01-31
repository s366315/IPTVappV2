package com.iptv.data.converters

import com.iptv.data.response.ChannelResponse
import com.iptv.data.response.GroupResponse
import com.iptv.domain.entities.Channel

fun ChannelResponse.toChannel(group: GroupResponse) =
    Channel(
        groupId = group.id,
        groupName = group.name,
        groupColor = group.color,
        id = id,
        name = name,
        isVideo = isVideo == "1",
        icon = icon,
        epgProgname = epgProgname ?: "",
        epgStart = epgStart?.toInt() ?: 0,
        epgEnd = epgEnd?.toInt() ?: 0
    )