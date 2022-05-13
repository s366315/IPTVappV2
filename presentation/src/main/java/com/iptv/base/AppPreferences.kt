package com.iptv.base

import android.content.Context
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.HttpCaching
import javax.inject.Inject

class AppPreferences @Inject constructor(context: Context) : Preferences {

    private val prefs = PreferencesHelper.defaultPrefs(context)

    companion object {
        const val PREF_AUTH_SSID = "PREF_AUTH_SSID"
        const val PREF_AUTH_SSID_NAME = "PREF_AUTH_SSID_NAME"
        const val PREF_LOGIN = "PREF_LOGIN"
        const val PREF_PASSWORD = "PREF_PASSWORD"
        const val PREF_PACKET_NAME = "PREF_PACKET_NAME"
        const val PREF_PACKET_ID = "PREF_PACKET_ID"
        const val PREF_PACKET_EXPIRE = "PREF_PACKET_EXPIRE"
        const val PREF_VOD_ENABLED = "PREF_VOD_ENABLED"
        const val PREF_ARCHIVE_ENABLED = "PREF_ARCHIVE_ENABLED"
        const val PREF_BUFFER = "PREF_BUFFER"
        const val PREF_BUFFER_LIST = "PREF_BUFFER_LIST"
    }

    override var sid: String = ""
        get() = requireNotNull(prefs[PREF_AUTH_SSID, ""])
        set(value) {
            prefs[PREF_AUTH_SSID] = value
            field = value
        }

    override var sidName: String = ""
        get() = requireNotNull(prefs[PREF_AUTH_SSID_NAME, ""])
        set(value) {
            prefs[PREF_AUTH_SSID_NAME] = value
            field = value
        }

    override var login: String = ""
        get() = requireNotNull(prefs[PREF_LOGIN, ""])
        set(value) {
            prefs[PREF_LOGIN] = value
            field = value
        }

    override var password: String = ""
        get() = requireNotNull(prefs[PREF_PASSWORD, ""])
        set(value) {
            prefs[PREF_PASSWORD] = value
            field = value
        }

    override var packetName: String = ""
        get() = requireNotNull(prefs[PREF_PACKET_NAME, ""])
        set(value) {
            prefs[PREF_PACKET_NAME] = value
            field = value
        }

    override var packetId: String = ""
        get() = requireNotNull(prefs[PREF_PACKET_ID, ""])
        set(value) {
            prefs[PREF_PACKET_ID] = value
            field = value
        }

    override var packetExpire: Int = 0
        get() = prefs.getInt(PREF_PACKET_EXPIRE, 0)
        set(value) {
            prefs[PREF_PACKET_EXPIRE] = value
            field = value
        }

    override var vod: Boolean = false
        get() = prefs.getBoolean(PREF_VOD_ENABLED, false)
        set(value) {
            prefs[PREF_VOD_ENABLED] = value
            field = value
        }

    override var archive: Boolean = false
        get() = prefs.getBoolean(PREF_ARCHIVE_ENABLED, false)
        set(value) {
            prefs[PREF_ARCHIVE_ENABLED] = value
            field = value
        }

    override var buffer: Int = 0
        get() = prefs.getInt(PREF_BUFFER, 0)
        set(value) {
            prefs[PREF_BUFFER] = value
            field = value
        }

    override var bufferList: String = ""
        get() = requireNotNull(prefs[PREF_BUFFER_LIST, ""])
        set(value) {
            prefs[PREF_BUFFER_LIST] = value
            field = value
        }

    override fun clean() {
        prefs.edit().clear().apply()
    }
}