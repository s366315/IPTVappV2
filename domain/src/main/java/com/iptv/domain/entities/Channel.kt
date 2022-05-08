package com.iptv.domain.entities

import java.io.Serializable

data class Channel(
    val groupId: String,
    val groupName: String,
    val groupColor: String,
    val id: String,
    val name: String,
    val isVideo: Boolean,
    val icon: String,
    val epgProgname: String,
    val epgStart: Int,
    val epgEnd: Int,
): Serializable
