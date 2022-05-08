package com.iptv

import android.content.Intent
import androidx.core.content.ContextCompat
import com.iptv.injection.components.DaggerApplicationComponent
import com.iptv.service.PlayerService
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()


    }
}