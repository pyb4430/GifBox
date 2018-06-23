package com.example.taylor.gifbox.module

import android.app.Application
import com.example.taylor.gifbox.controller.ApiController
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.model.MyObjectBox
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import javax.inject.Singleton

/**
 * Created by Taylor on 1/29/2018.
 */
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): BoxStore {
        return MyObjectBox.builder().androidContext(application).build()
    }

    @Singleton
    @Provides
    fun provideDataController(database: BoxStore, apiController: ApiController): DataController {
        return DataController(database, apiController)
    }
}