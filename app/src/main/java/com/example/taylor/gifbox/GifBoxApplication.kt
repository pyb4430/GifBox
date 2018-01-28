package com.example.taylor.gifbox

import android.app.Application

/**
 * Created by Taylor on 1/27/2018.
 */
class GifBoxApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}