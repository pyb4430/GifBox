package com.example.taylor.gifbox.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.example.taylor.gifbox.GifBoxApplication
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.model.PaginationObject
import com.example.taylor.gifbox.model.Tag
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class MainActivityVM(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var dataController: DataController

    val trendingGifPagedList: LiveData<PagedList<Gif>>
    val pagingCallback: TrendingBoundaryCallback

    val trendingLoading = MutableLiveData<Boolean>()

    init {
        (application as GifBoxApplication).appComponent.inject(this)

        trendingLoading.value = false

        pagingCallback = TrendingBoundaryCallback(application, trendingLoading)
        trendingGifPagedList = LivePagedListBuilder(dataController.getAllGifsPaginated(), 10)
                .setBoundaryCallback(pagingCallback)
                .build()
    }

    fun refreshTrending() {
        dataController.clearGifTags(Tag.TRENDING)
    }
}

class TrendingBoundaryCallback(application: GifBoxApplication,
                               private val firstPageLoading: MutableLiveData<Boolean>) : PagedList.BoundaryCallback<Gif>() {

    @Inject
    lateinit var dataController: DataController

    var disposable: Disposable? = null
    var pagination: PaginationObject? = null

    init {
        application.appComponent.inject(this)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Gif) {
        doFetch()
    }

    override fun onZeroItemsLoaded() {
        doFetch(true)
    }

    private fun doFetch(isInitial: Boolean = false) {
        val disposable = disposable
        if (disposable == null || disposable.isDisposed) {
            var offset = 0
            if (!isInitial) {
                pagination?.let {
                    offset = it.offset + it.count
                }
            } else {
                firstPageLoading.postValue(true)
            }
            this.disposable = dataController.fetchTrending(offset).subscribe(
                    {
                        pagination = it
                        firstPageLoading.postValue(false)
                    },
                    {
                        Timber.e(it)
                        firstPageLoading.postValue(false)
                    }
            )
        }
    }
}