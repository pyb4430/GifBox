package com.example.taylor.gifbox

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().
                client(client).
                baseUrl(baseUrl).
                addConverterFactory(MoshiConverterFactory.create()).
                build()
    }

    @Singleton
    @Provides
    fun provideGiphyApi(retrofit: Retrofit): GiphyApi {
        return retrofit.create(GiphyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApiController(giphyApi: GiphyApi): ApiController {
        return ApiController(giphyApi, apiKey)
    }
}