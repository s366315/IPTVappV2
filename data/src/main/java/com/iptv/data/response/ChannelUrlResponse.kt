package com.iptv.data.response

import com.google.gson.annotations.SerializedName

data class ChannelUrlResponse(
    @SerializedName("url") val url: String?
) : ModelResponse()