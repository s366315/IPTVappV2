package com.iptv.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.iptv.data.preferences.Preferences
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object ApiServiceFactory {
    private const val CONNECTION_TIMEOUT_MS = 5_000L
    private const val READ_WRITE_TIMEOUT_MS = 5_000L

    fun newInstance(
        endpoint: String,
        settings: Preferences
    ): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(endpoint)
            .client(okHttpClient(settings))
            .addConverterFactory(GsonConverterFactory.create(buildGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun okHttpClient(
        settings: Preferences
    ): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            readTimeout(READ_WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            writeTimeout(READ_WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            addInterceptor { chain ->
                val request = chain.request()
                val builder = request
                    .newBuilder()
                    .header("Content-Type", "application/json")
                    .header("ReturnFormat", "mobile")
                    .method(request.method, request.body)
                val mutatedRequest = builder.build()
                val response = chain.proceed(mutatedRequest)

                response
            }
            addInterceptor(loggingInterceptor)
            addInterceptor(authorizationInterceptor(settings))
//            addInterceptor(chuckerInterceptor)
        }.build()
    }

    private fun authorizationInterceptor(settings: Preferences) = Interceptor {
        val url = it.request().url.newBuilder().addQueryParameter("MWARE_SSID", settings.sid).build()
        it.proceed(it.request().newBuilder().url(url).build())
    }

    private fun buildGson(): Gson {
        return GsonBuilder()
            .setPrettyPrinting()
            .create()
    }
}