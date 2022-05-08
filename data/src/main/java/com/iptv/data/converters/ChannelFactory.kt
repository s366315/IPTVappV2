package com.iptv.data.converters

import com.iptv.data.response.Channel
import com.iptv.data.response.Group

fun Channel.toChannel(group: Group) =
    com.iptv.domain.entities.Channel(
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