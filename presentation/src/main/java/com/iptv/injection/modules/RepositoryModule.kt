package com.iptv.injection.modules

import com.iptv.data.repository.ChannelListRepositoryImpl
import com.iptv.data.repository.ChannelUrlRepositoryImpl
import com.iptv.data.repository.ChannelsByIdRepositoryImpl
import com.iptv.data.repository.LoginRepositoryImpl
import com.iptv.domain.repository.ChannelListRepository
import com.iptv.domain.repository.ChannelUrlRepository
import com.iptv.domain.repository.ChannelsByIdRepository
import com.iptv.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    internal abstract fun loginRepository(repository: LoginRepositoryImpl): LoginRepository

    @Binds
    internal abstract fun channelListRepository(repository: ChannelListRepositoryImpl): ChannelListRepository

    @Binds
    internal abstract fun channelUrlRepository(repository: ChannelUrlRepositoryImpl): ChannelUrlRepository

    @Binds
    internal abstract fun channelsByIdRepository(repository: ChannelsByIdRepositoryImpl): ChannelsByIdRepository
}