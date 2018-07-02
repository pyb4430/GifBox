package com.example.taylor.gifbox.controller

import android.arch.lifecycle.LiveData
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.module.GifBoxDatabase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

/**
 * Created by Taylor on 1/29/2018.
 */
class DataControllerImpl(private val db: GifBoxDatabase, private val apiController: ApiController): DataController {

    // api calls

    override fun fetchTrending() {
        apiController.fetchTrending(10, "R")
                .subscribe(
                        {
                            writeGifMetaDatas(it.gifList)
                        },
                        { Timber.d(it) }
                )
    }


    // db writes

    private fun writeGifMetaDatas(gifs: List<Gif>) {
        Completable.fromCallable {
                    db.gifDao().insertAll(*gifs.toTypedArray())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }


    // db reads

    override fun getAllGifs(): LiveData<List<Gif>> {
        return db.gifDao().getAll()
    }
}

interface DataController {
    fun fetchTrending()
    fun getAllGifs(): LiveData<List<Gif>>
}