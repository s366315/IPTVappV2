package com.iptv.injection.modules

import com.iptv.domain.interactor.channelList.ChannelListUseCase
import com.iptv.domain.interactor.channelUrl.ChannelUrlUseCase
import com.iptv.domain.interactor.channelsById.ChannelsByIdUseCase
import com.iptv.domain.interactor.signin.LoginUseCase
import com.iptv.domain.repository.ChannelListRepository
import com.iptv.domain.repository.ChannelUrlRepository
import com.iptv.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides

@Module(includes = [RepositoryModule::class])
open class UseCaseModule {
    @Provides
    fun loginUseCase(loginRepository: LoginRepository): LoginUseCase {
        return LoginUseCase(loginRepository)
    }

    @Provides
    fun channelListUseCase(channelListRepository: ChannelListRepository): ChannelListUseCase {
        return ChannelListUseCase(channelListRepository)
    }

    @Provides
    fun channelsByIdUseCase(channelListRepository: ChannelListRepository): ChannelsByIdUseCase {
        return ChannelsByIdUseCase(channelListRepository)
    }

    @Provides
    fun channelUrlUseCase(channelUrlRepository: ChannelUrlRepository): ChannelUrlUseCase {
        return ChannelUrlUseCase(channelUrlRepository)
    }
}