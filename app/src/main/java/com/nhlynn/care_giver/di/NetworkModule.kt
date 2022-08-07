package com.nhlynn.care_giver.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nhlynn.care_giver.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun gSon(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    fun client(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val origin: Request = chain.request()
                val requestBuilder: Request.Builder = origin.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept", "text/plain")
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            })
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(gSon: Gson, client: OkHttpClient): ApiService =
        Retrofit
            .Builder()
            .baseUrl("http://192.168.10.175/caregiver/") //base url change your server url
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .build()
            .create(ApiService::class.java)
}