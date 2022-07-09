package com.iptv.data.api

import com.iptv.data.response.ChannelUrlResponse
import com.iptv.data.response.ChannelsResponse
import com.iptv.data.response.LoginResponse
import com.iptv.data.response.SuccessOperation
import com.iptv.domain.entities.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("login")
    suspend fun login(
        @Query("login") login: String,
        @Query("pass") password: String,
        @Query("softid") softId: String,
        @Query("cli_serial") cliSerial: String,
        @Query("settings") settings: String
    ): LoginResponse

    @GET("channel_list")
    suspend fun channelList(
        @Query("show") showProtected: String?,
        @Query("protect_code") protectedCode: String?,
    ): ChannelsResponse

    @GET("epg_current")
    suspend fun channelsById(
        @Query("epg") epg: String? = "2",
        @Query("cids") cids: String,
    ): ChannelsResponse

    @GET("get_url")
    suspend fun getUrl(
        @Query("cid") channelId: String
    ): ChannelUrlResponse

    @GET("settings_set")
    suspend fun setSettings(
        @Query("var") variant: String,
        @Query("val") value: String
    ): SuccessOperation

    //settings_set?var=<pcode|http_caching|stream_server|timeshift|timezone>&val=<value>
}