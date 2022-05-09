package com.iptv.domain.interactor.channelList

import com.iptv.domain.entities.Channel
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.UseCase
import com.iptv.domain.repository.ChannelListRepository

class ChannelListUseCase(private val channelListRepository: ChannelListRepository) :
    UseCase<ChannelListUseCase.Params, List<Channel>>() {

    data class Params(
        val showProtected: Boolean? = null,
        val protectedCode: String? = null
    )

    override suspend fun createObservable(params: Params?): Result<List<Channel>> {
        return channelListRepository.channelList(
            showProtected = if (params?.showProtected == true) "1" else null,
            protectedCode = params?.protectedCode
        )
    }
}