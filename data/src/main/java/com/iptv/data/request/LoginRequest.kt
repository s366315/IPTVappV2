package com.iptv.data.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val login: String,
    val password: String,
    val softId: String,
    @SerializedName("cli_serial")
    val cliSerial: String
)