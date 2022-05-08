package com.iptv.data.response

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T : ModelResponse>(
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("error")
    val error: Any?,
    @SerializedName("status")
    val status: String,
    @SerializedName("data", alternate = ["items"])
    val data: T
)