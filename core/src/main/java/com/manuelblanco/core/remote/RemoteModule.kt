package com.manuelblanco.core.remote

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.manuelblanco.core.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

internal val remoteModule = module {
    single { provideApi<MarvelApi>(okHttpClient = get(), gson = get()) }
    factory { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }
    single { provideOkHttpClient(loggingInterceptor = get()) }
    single { provideGson() }
    single { provideCache(androidApplication()) }
}

private fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }

        builder.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val publicKey = BuildConfig.PUBLIC_API_KEY
            val privateKey = BuildConfig.PRIVATE_API_KEY

            val ts = System.currentTimeMillis()
            val beforeHash = ts.toString() + publicKey + privateKey

            val md = MessageDigest.getInstance("MD5")
            val digested = md.digest(beforeHash.toByteArray()) // making md5 hash bytes
            val hash = digested.joinToString("") {
                String.format("%02x", it) // md5 bytes to string
            }

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("ts", ts.toString())
                .addQueryParameter("apikey", publicKey)
                .addQueryParameter("hash", hash)
                .build()

            val requestBuilder = original.newBuilder().url(url)

            chain.proceed(requestBuilder.build())
        }

        return builder.build()
}

private inline fun <reified T> provideApi(
    okHttpClient: OkHttpClient,
    gson: Gson
): T = Retrofit.Builder().apply {
    baseUrl("https://gateway.marvel.com/")
    client(okHttpClient)
    addCallAdapterFactory(CoroutineCallAdapterFactory())
    addConverterFactory(GsonConverterFactory.create(gson))
}.build().create(T::class.java)

private fun provideGson(): Gson {
    return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
}

private fun provideCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
}