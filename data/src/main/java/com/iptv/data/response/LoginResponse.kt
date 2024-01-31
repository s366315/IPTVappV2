package com.iptv.data.response

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("sid") val sid: String?,
    @SerializedName("sid_name") val sidName: String?,
    @SerializedName("account") val account: AccountResponse?,
    @SerializedName("services") val services: ServicesResponse?,
    @SerializedName("settings") val settings: SettingsResponse?
) : ModelResponse()

data class AccountResponse(
    @SerializedName("login") val login: String,
    @SerializedName("packet_name") val packetName: String,
    @SerializedName("packet_id") val packetId: String,
    @SerializedName("packet_expire") val packetExpire: Int
)

data class ServicesResponse(
    @SerializedName("vod") val vod: Int?,
    @SerializedName("archive") val archive: Int?,
    @SerializedName("start_film") val startFilm: Int?
)

data class SettingsResponse(
    @SerializedName("http_caching") val httpCaching: HttpCachingResponse?
)

data class HttpCachingResponse(
    @SerializedName("value") val value: String?,
    @SerializedName("list") val list: List<Int>?
)

fun List<Int>.toJString() = Gson().toJson(this)