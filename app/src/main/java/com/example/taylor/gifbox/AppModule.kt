package com.example.taylor.gifbox

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Taylor on 1/27/2018.
 */
@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideUserPrefs(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }
}