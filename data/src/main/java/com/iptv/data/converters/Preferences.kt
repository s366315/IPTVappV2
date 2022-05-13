package com.iptv.data.converters

import com.iptv.converters.toStringList
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.*

fun Preferences.toLoginDetails() =
    LoginDetails(
        sid = "",
        sidName = "",
        account = Account(
            login = login,
            packetName = packetName,
            packetId = packetId,
            packetExpire = packetExpire
        ),
        services = Services(
            vod = vod,
            archive = archive
        ),
        settings = Settings(
            httpCaching = HttpCaching(
                value = buffer,
                list = bufferList.toStringList().toList()
            )
        )
    )