package com.iptv.utils

import java.text.SimpleDateFormat
import java.util.*

fun Int.timestampToEpg(): String {
    return SimpleDateFormat("H:mm", Locale.getDefault()).format(Date(this.toLong().times(1000)))
}

fun Int.timestampToDate(): String {
    return SimpleDateFormat("Y.MM.dd H:mm", Locale.getDefault()).format(Date(this.toLong().times(1000)))
}

fun timestampToTimeFormatted(sec: Long, pattern: String): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(sec * 1000))
}