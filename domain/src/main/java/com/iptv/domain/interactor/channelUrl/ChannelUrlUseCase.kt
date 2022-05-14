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

    override suspend fun createObservable(
        params: Params?,
        onSuccess: suspend (Result.Success<ChannelUrl>) -> Unit,
        onError: suspend (Result.Error) -> Unit
    ) {
        params?.let {
            val result = channelUrlRepository.getUrl(
                channelId = params.channelId
            )

            when (result) {
                is Result.Success -> {
                    onSuccess(result)
                }
                is Result.Error -> {
                    onError(result)
                }
            }
        } ?: throw IllegalStateException("ChannelUrlUseCase.Params must not be null")
    }
}