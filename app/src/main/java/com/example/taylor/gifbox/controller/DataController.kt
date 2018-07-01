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
class DataController(private val db: GifBoxDatabase, private val apiController: ApiController) {

    // api calls

    fun fetchTrending() {
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

    fun getAllGifs(): LiveData<List<Gif>> {
        return db.gifDao().getAll()
    }

}