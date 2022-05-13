package com.iptv.domain.entities

data class LoginDetails(
    val sid: String,
    val sidName: String,
    val account: Account,
    val services: Services,
    val settings: Settings
)

data class Account(
    val login: String,
    val packetName: String,
    val packetId: String,
    val packetExpire: Number
)

data class Services(
    val vod: Boolean,
    val archive: Boolean
)

data class Settings(
    val httpCaching: HttpCaching
)

data class HttpCaching(
    val value: Int,
    val list: List<Int>
)
