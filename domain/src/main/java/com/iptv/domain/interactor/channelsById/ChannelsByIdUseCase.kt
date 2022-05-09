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

    override suspend fun createObservable(params: Params?): Result<List<Channel>> {
        return channelListRepository.channelListById(params?.cids ?: emptyList())
    }
}