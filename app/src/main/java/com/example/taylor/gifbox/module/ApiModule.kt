package com.example.taylor.gifbox.module

import com.example.taylor.gifbox.controller.GiphyApi
import com.example.taylor.gifbox.controller.ApiController
import com.example.taylor.gifbox.controller.ApiControllerImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by Taylor on 1/28/2018.
 */
@Module
class ApiModule(val baseUrl: String, val apiKey: String, val logLevel: HttpLoggingInterceptor.Level) {

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(logLevel)).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder().
                client(client).
                baseUrl(baseUrl).
                addConverterFactory(MoshiConverterFactory.create(moshi)).
                addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())).
                build()
    }

    @Singleton
    @Provides
    fun provideGiphyApi(retrofit: Retrofit): GiphyApi {
        return retrofit.create(GiphyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun provideApiController(giphyApi: GiphyApi): ApiController {
        return ApiControllerImpl(giphyApi, apiKey)
    }
}