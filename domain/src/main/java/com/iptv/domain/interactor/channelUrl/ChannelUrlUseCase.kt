package com.iptv.domain.interactor.channelUrl

import com.iptv.domain.entities.ChannelUrl
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.UseCase
import com.iptv.domain.repository.ChannelUrlRepository

class ChannelUrlUseCase(
    private val channelUrlRepository: ChannelUrlRepository
) : UseCase<ChannelUrlUseCase.Params, ChannelUrl>() {

    data class Params(
        val channelId: String
    )

    override suspend fun createObservable(params: Params?): Result<ChannelUrl> {
        params?.let {
            return channelUrlRepository.getUrl(
                channelId = params.channelId
            )
        } ?: throw IllegalStateException("ChannelUrlUseCase.Params must not be null")
    }
}