package com.example.taylor.gifbox.controller

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.model.PaginationObject
import com.example.taylor.gifbox.model.Tag
import com.example.taylor.gifbox.module.GifBoxDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Taylor on 1/29/2018.
 */
class DataControllerImpl(private val db: GifBoxDatabase,
                         private val apiController: ApiController): DataController {

    init {
        Completable.fromCallable {
            db.tagDao().insertAll(*Tag.TAGS)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    // api calls

    override fun fetchTrending(offset: Int): Observable<PaginationObject> {
        return apiController.fetchTrending(20, "R", offset)
                .flatMap {
                    writeGifMetaDatas(it.gifList, Tag.TRENDING, offset)
                    Timber.d("pagination data: ${it.pagination}, ${it.pagination.offset}")
                    Observable.just(it.pagination)
                }.subscribeOn(Schedulers.io())
    }


    // db writes

    private fun writeGifMetaDatas(gifs: List<Gif>, tag: String, offset: Int) {
        val gifArray = gifs.toTypedArray()
        db.gifDao().insertAll(*gifArray)
        db.gifTagDao().tagGifs(tag, offset, *gifArray)
    }

    override fun clearGifTags(tag: String) {
        Completable.fromCallable {
            db.gifTagDao().clearTag(tag)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    // db reads

    override fun getAllGifs(): LiveData<List<Gif>> {
        return db.gifDao().getAll()
    }

    override fun getAllGifsPaginated(): DataSource.Factory<Int, Gif> {
        return db.gifDao().getAllPaginated()
    }
}

interface DataController {
    fun fetchTrending(offset: Int = 0): Observable<PaginationObject>
    fun getAllGifs(): LiveData<List<Gif>>
    fun getAllGifsPaginated(): DataSource.Factory<Int, Gif>
    fun clearGifTags(tag: String)
}