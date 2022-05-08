package com.iptv.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val sid: String?,
    @SerializedName("sid_name")
    val sidName: String?,
    val account: Account?,
    val services: Services?
) : ModelResponse()

data class Account(
    val login: String,
    @SerializedName("packet_name")
    val packetName: String,
    @SerializedName("packet_id")
    val packetId: String,
    @SerializedName("packet_expire")
    val packetExpire: Int
)

data class Services(
    val vod: Int?,
    val archive: Int?,
    @SerializedName("start_film")
    val startFilm: Int?
)