package com.example.taylor.gifbox.module

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.example.taylor.gifbox.BuildConfig
import com.example.taylor.gifbox.controller.ApiController
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.controller.DataControllerImpl
import com.example.taylor.gifbox.model.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlin.concurrent.thread

/**
 * Created by Taylor on 1/29/2018.
 */
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): GifBoxDatabase {
        val db = if (BuildConfig.DEBUG)
            Room.inMemoryDatabaseBuilder(application, GifBoxDatabase::class.java).build() else
            Room.databaseBuilder(application, GifBoxDatabase::class.java, "gifbox_database").build()
        thread {
            db.tagDao().insertAll(*Tag.TAGS)
        }
        return db
    }

    @Singleton
    @Provides
    fun provideDataController(database: GifBoxDatabase, apiController: ApiController): DataController {
        return DataControllerImpl(database, apiController)
    }
}

@Database(entities = [Gif::class, Tag::class, GifTagJoin::class], version = 1)
abstract class GifBoxDatabase: RoomDatabase() {
    abstract fun gifDao(): GifDao
    abstract fun tagDao(): TagDao
    abstract fun gifTagDao(): GifTagDao
}