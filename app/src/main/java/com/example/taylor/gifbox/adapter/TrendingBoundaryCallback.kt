package com.example.taylor.gifbox.adapter

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.example.taylor.gifbox.GifBoxApplication
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.response.PaginationObject
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class TrendingBoundaryCallback(application: GifBoxApplication,
                               private val firstPageLoading: MutableLiveData<Boolean>) : PagedList.BoundaryCallback<Gif>() {

    @Inject
    lateinit var dataController: DataController

    private var disposable: Disposable? = null
    private var pagination: PaginationObject? = null

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