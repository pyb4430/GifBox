package com.example.taylor.gifbox

import android.app.Application
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Created by Taylor on 1/27/2018.
 */
class GifBoxApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent.builder().
                appModule(AppModule(this)).
                apiModule(ApiModule(getString(R.string.giphy_api_base_url),
                        getString(R.string.giphy_api_key),
                        HttpLoggingInterceptor.Level.BODY)).
                build()
    }
}