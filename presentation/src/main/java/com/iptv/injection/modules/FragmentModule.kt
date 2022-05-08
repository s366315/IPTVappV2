package com.iptv.injection.modules

import com.iptv.injection.scopes.PerFragment
import com.iptv.live.LiveFragment
import com.iptv.livePlayer.LivePlayerFragment
import com.iptv.signin.SignInFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    @PerFragment
    abstract fun liveFragment(): LiveFragment

    @ContributesAndroidInjector
    @PerFragment
    abstract fun livePlayerFragment(): LivePlayerFragment

    @ContributesAndroidInjector
    @PerFragment
    abstract fun signInFragment(): SignInFragment
}