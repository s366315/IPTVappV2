package com.iptv.data.api

import com.iptv.data.response.ChannelUrlResponse
import com.iptv.data.response.ChannelsResponse
import com.iptv.data.response.LoginResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("login")
    suspend fun login(
        @Query("login") login: String,
        @Query("pass") password: String,
        @Query("softid") softId: String,
        @Query("cli_serial") cliSerial: String
    ): LoginResponse

    @GET("channel_list")
    suspend fun channelList(
        @Query("MWARE_SSID") ssid: String,
        @Query("show") showProtected: String?,
        @Query("protect_code") protectedCode: String?,
    ): ChannelsResponse

    @GET("epg_current")
    suspend fun channelsById(
        @Query("MWARE_SSID") ssid: String,
        @Query("epg") epg: String? = "2",
        @Query("cids") cids: String,
    ): ChannelsResponse

    @GET("get_url")
    suspend fun getUrl(
        @Query("MWARE_SSID") ssid: String,
        @Query("cid") channelId: String
    ): ChannelUrlResponse
}