package com.iptv.injection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iptv.SignInFragmentViewModel
import com.iptv.injection.ViewModelFactory
import com.iptv.injection.scopes.ViewModelKey
import com.iptv.live.LiveFragmentViewModel
import com.iptv.live.LiveFragmentViewModelImpl
import com.iptv.livePlayer.LivePlayerViewModel
import com.iptv.settings.SettingsFragmentViewModel
import com.iptv.settings.SettingsFragmentViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [UseCaseModule::class])
abstract class ViewModelModule {

    @Binds
    internal abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LiveFragmentViewModel::class)
    internal abstract fun liveViewModel(viewModel: LiveFragmentViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LivePlayerViewModel::class)
    internal abstract fun livePlayerViewModel(viewModel: LivePlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInFragmentViewModel::class)
    internal abstract fun mainActivityViewModel(viewModel: SignInFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsFragmentViewModel::class)
    internal abstract fun settingsViewModel(viewModel: SettingsFragmentViewModelImpl): ViewModel
}