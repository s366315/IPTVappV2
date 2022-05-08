package com.iptv.data.preferences

interface Preferences {
    var sid: String
    var sidName: String
    var login: String
    var password: String
    var packetName: String
    var packetId: String
    var packetExpire: Int
    var vod: Boolean
    var archive: Boolean
    fun clean()
}