package com.iptv.injection.components

import com.iptv.App
import com.iptv.injection.modules.ActivityModule
import com.iptv.injection.modules.ApplicationModule
import com.iptv.injection.modules.FragmentModule
import com.iptv.injection.modules.RepositoryModule
import com.iptv.injection.modules.ServiceModule
import com.iptv.injection.modules.ViewModelModule
import dagger.Component
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