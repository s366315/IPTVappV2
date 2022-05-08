package com.iptv.data.response

import com.google.gson.annotations.SerializedName

data class ChannelsResponse(
    val groups: List<Group>?
) : ModelResponse()

data class Group(
    val id: String,
    val name: String,
    val color: String,
    val channels: List<Channel>
)

data class Channel(
    val id: String,
    val name: String,
    @SerializedName("is_video")
    val isVideo: String,
    val icon: String,
    @SerializedName("epg_progname")
    val epgProgname: String?,
    @SerializedName("epg_start")
    val epgStart: Number?,
    @SerializedName("epg_end")
    val epgEnd: Number?,
)