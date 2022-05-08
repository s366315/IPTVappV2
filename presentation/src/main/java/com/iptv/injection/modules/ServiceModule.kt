package com.iptv.injection.modules

import com.iptv.injection.scopes.PerService
import com.iptv.service.PlayerService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    @PerService
    abstract fun playerService(): PlayerService
}
