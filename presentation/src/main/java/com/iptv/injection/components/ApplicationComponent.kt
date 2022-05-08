package com.iptv.injection.components

import com.iptv.App
import com.iptv.injection.modules.*
import dagger.Component
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    AndroidSupportInjectionModule::class,
    FragmentModule::class,
    RepositoryModule::class,
    ViewModelModule::class,
    ActivityModule::class,
    ServiceModule::class
])

interface ApplicationComponent: AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}