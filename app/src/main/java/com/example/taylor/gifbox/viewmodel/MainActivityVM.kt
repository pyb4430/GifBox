package com.example.taylor.gifbox.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.example.taylor.gifbox.GifBoxApplication
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.model.PaginationObject
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class MainActivityVM(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var dataController: DataController

    val trendingGifs: LiveData<List<Gif>>
    val trendingGifPagedList: LiveData<PagedList<Gif>>

    init {
        (application as GifBoxApplication).appComponent.inject(this)

        trendingGifs = dataController.getAllGifs()
        trendingGifPagedList = LivePagedListBuilder(dataController.getAllGifsPaginated(), 10)
                .setBoundaryCallback(TrendingBoundaryCallback(application))
                .build()
    }

    fun fetchTrending() {
        dataController.fetchTrending()
    }
}

class TrendingBoundaryCallback(application: GifBoxApplication) : PagedList.BoundaryCallback<Gif>() {

    @Inject
    lateinit var dataController: DataController

    var disposable: Disposable? = null
    var pagination: PaginationObject? = null

    init {
        application.appComponent.inject(this)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Gif) {
        doFetch(false)
    }

    override fun onZeroItemsLoaded() {
        doFetch(true)
    }

    private fun doFetch(isInitial: Boolean = true) {
        val disposable = disposable
        Timber.d("initial load: $isInitial ${disposable?.isDisposed}")
        if (disposable == null || disposable.isDisposed) {
            var offset = 0
            if (!isInitial) {
                pagination?.let {
                    offset = it.offset + it.count
                }
            }
            this.disposable = dataController.fetchTrending(offset).subscribe(
                    {
                        pagination = it
                    },
                    {
                        Timber.e(it)
                    }
            )
        }
    }
}