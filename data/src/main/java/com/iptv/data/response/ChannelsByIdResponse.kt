package com.iptv.data.response

import com.google.gson.annotations.SerializedName

data class ChannelsByIdResponse(
    @SerializedName("epg") val epg: List<EpgContainerResponse>
) : ModelResponse()

data class EpgContainerResponse(
    @SerializedName("cid") val cid: Int,
    @SerializedName("epg") val epg: List<EpgDataResponse>
)

data class EpgDataResponse(
    @SerializedName("ts") val ts: String,
    @SerializedName("progname") val progname: String
)