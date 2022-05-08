package com.iptv.data.response

data class ChannelsByIdResponse(
    val epg: List<EpgContainer>
) : ModelResponse()

data class EpgContainer(
    val cid: Int,
    val epg: List<EpgData>
)

data class EpgData(
    val ts: String,
    val progname: String
)