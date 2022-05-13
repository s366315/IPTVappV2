package com.iptv.injection.modules

import android.content.Context
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsCollector
import com.google.android.exoplayer2.analytics.DefaultAnalyticsCollector
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.Clock
import com.iptv.App
import com.iptv.BuildConfig
import com.iptv.base.AppPreferences
import com.iptv.data.api.ApiService
import com.iptv.data.api.ApiServiceFactory
import com.iptv.data.preferences.Preferences
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
open class ApplicationModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideChucker(app: App): Interceptor {
        return Interceptor { chain -> chain.proceed(chain.request()) }
    }

    @Provides
    fun provideApiClient(
        chuckerInterceptor: Interceptor
    ): ApiService {
        return ApiServiceFactory.newInstance(
            BuildConfig.API_URL,
            chuckerInterceptor
        )
    }

    @Singleton
    @Provides
    fun providePlayer(context: Context, settings: Preferences): ExoPlayer {
        val defaultBuffer = settings.buffer
        val loadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
            defaultBuffer, defaultBuffer, defaultBuffer, defaultBuffer
        ).build()

        return ExoPlayer.Builder(
            context,
            DefaultRenderersFactory(context),
            DefaultMediaSourceFactory(context),
            DefaultTrackSelector(context),
            loadControl,
            DefaultBandwidthMeter.Builder(context).build(),
            DefaultAnalyticsCollector(Clock.DEFAULT)
        ).build()
    }

    @Singleton
    @Provides
    fun providePreferences(app: App): Preferences {
        return AppPreferences(app)
    }
}