package com.iptv.data.response

import com.google.gson.annotations.SerializedName

data class ChannelsResponse(
    @SerializedName("groups") val groups: List<GroupResponse>?
) : ModelResponse()

data class GroupResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String,
    @SerializedName("channels") val channels: List<ChannelResponse>
)

data class ChannelResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("is_video") val isVideo: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("epg_progname") val epgProgname: String?,
    @SerializedName("epg_start") val epgStart: Number?,
    @SerializedName("epg_end") val epgEnd: Number?,
)