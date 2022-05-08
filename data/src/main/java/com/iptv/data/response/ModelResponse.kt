package com.iptv.data.response

abstract class ModelResponse {
    val error: Error? = null
}

data class Error(
    val code: Number,
    val message: String
)