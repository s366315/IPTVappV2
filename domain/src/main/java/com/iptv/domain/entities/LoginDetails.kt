package com.iptv.domain.entities

data class LoginDetails(
    val sid: String,
    val sidName: String,
    val account: Account,
    val services: Services
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
