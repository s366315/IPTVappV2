package com.iptv.injection.modules

import com.iptv.MainActivity
import com.iptv.injection.scopes.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    @PerActivity
    abstract fun mainActivity(): MainActivity

}
