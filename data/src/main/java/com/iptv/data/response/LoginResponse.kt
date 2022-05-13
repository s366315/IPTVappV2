package com.iptv.data.response

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val sid: String?,
    @SerializedName("sid_name")
    val sidName: String?,
    val account: Account?,
    val services: Services?,
    val settings: Settings?
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

data class Settings(
    @SerializedName("http_caching")
    val httpCaching: HttpCaching?
)

data class HttpCaching(
    val value: String?,
    val list: List<Int>?
)

fun List<Int>.toJString() = Gson().toJson(this)