package com.iptv.data.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("text") val text: String,
    @SerializedName("code") val code: Int
)
