package com.iptv.converters

import com.google.gson.Gson
import com.iptv.domain.entities.HttpCaching

fun String.toHttpCaching() = Gson().fromJson(this, HttpCaching::class.java)

fun String.toStringList() = Gson().fromJson(this, Array<Int>::class.java)
