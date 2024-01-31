package com.iptv.data.response

import com.google.gson.annotations.SerializedName

abstract class ModelResponse {
    @SerializedName("error") val error: Error? = null
}

data class Error(
    @SerializedName("code") val code: Number,
    @SerializedName("message") val message: String
)