package com.example.taylor.gifbox

import com.example.taylor.gifbox.module.ApiModule
import com.example.taylor.gifbox.module.AppModule
import com.example.taylor.gifbox.module.DataModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Taylor on 1/27/2018.
 */
@Singleton
@Component(modules = [AppModule::class, ApiModule::class, DataModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}