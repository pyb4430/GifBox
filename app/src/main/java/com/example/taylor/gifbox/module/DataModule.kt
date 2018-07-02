package com.example.taylor.gifbox.module

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.example.taylor.gifbox.controller.ApiController
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.controller.DataControllerImpl
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.model.GifDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Taylor on 1/29/2018.
 */
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): GifBoxDatabase {
        return Room.databaseBuilder(application, GifBoxDatabase::class.java, "gifbox_database").build()
    }

    @Singleton
    @Provides
    fun provideDataController(database: GifBoxDatabase, apiController: ApiController): DataController {
        return DataControllerImpl(database, apiController)
    }
}

@Database(entities = [Gif::class], version = 1)
abstract class GifBoxDatabase: RoomDatabase() {
    abstract fun gifDao(): GifDao
}