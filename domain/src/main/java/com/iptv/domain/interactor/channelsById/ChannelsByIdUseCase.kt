package com.iptv.domain.interactor.channelsById

import com.iptv.domain.entities.Channel
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.UseCase
import com.iptv.domain.repository.ChannelListRepository

class ChannelsByIdUseCase(private val channelListRepository: ChannelListRepository) :
    UseCase<ChannelsByIdUseCase.Params, List<Channel>>() {

    data class Params(
        val cids: List<String>
    )

    override suspend fun createObservable(
        params: Params?,
        onSuccess: suspend (Result.Success<List<Channel>>) -> Unit,
        onError: suspend (Result.Error) -> Unit
    ) {
        val result = channelListRepository.channelListById(params?.cids ?: emptyList())

        when (result) {
            is Result.Success -> {
                onSuccess(result)
            }
            is Result.Error -> {
                onError(result)
            }
            else -> {}
        }
    }
}