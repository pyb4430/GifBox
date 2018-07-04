package com.example.taylor.gifbox.controller

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.model.PaginationObject
import com.example.taylor.gifbox.module.GifBoxDatabase
import com.example.taylor.gifbox.module.PaginationMap
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

/**
 * Created by Taylor on 1/29/2018.
 */
class DataControllerImpl(private val db: GifBoxDatabase, private val apiController: ApiController, private val paginationMap: PaginationMap): DataController {

    // api calls

    override fun fetchTrending(offset: Int): Observable<PaginationObject> {
        return apiController.fetchTrending(20, "R", offset)
                .flatMap {
                    writeGifMetaDatas(it.gifList)
                    Timber.d("pagination data: ${it.pagination}, ${it.pagination.offset}")
                    Observable.just(it.pagination)
                }
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

    override fun getAllGifsPaginated(): DataSource.Factory<Int, Gif> {
        return db.gifDao().getAllPaginated()
    }
}

interface DataController {
    fun fetchTrending(offset: Int = 0): Observable<PaginationObject>
    fun getAllGifs(): LiveData<List<Gif>>
    fun getAllGifsPaginated(): DataSource.Factory<Int, Gif>
}