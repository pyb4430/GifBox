package com.example.taylor.gifbox

import dagger.Component
import javax.inject.Singleton

/**
 * Created by Taylor on 1/27/2018.
 */
@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}