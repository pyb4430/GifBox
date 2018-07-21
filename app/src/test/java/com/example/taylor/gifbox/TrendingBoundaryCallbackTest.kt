package com.example.taylor.gifbox

import android.arch.lifecycle.MutableLiveData
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.model.GifImageData
import com.example.taylor.gifbox.model.PaginationObject
import com.example.taylor.gifbox.viewmodel.TrendingBoundaryCallback
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TrendingBoundaryCallbackTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    val testAppComponent = TestAppComponent()
    val dataController = testAppComponent.dataController

    @Mock
    lateinit var application: GifBoxApplication
    @Mock
    lateinit var mockLiveData: MutableLiveData<Boolean>
    val fakeGif = Gif("88y", "fake.url", GifImageData(null, null, null, null))

    lateinit var trendingBoundaryCallback: TrendingBoundaryCallback

    @Before
    fun setUp() {
        application.appComponent = testAppComponent

        whenever(dataController.fetchTrending(anyInt())).thenAnswer {
            Observable.just(PaginationObject(it.getArgument<Int>(0), 100, 10))
        }

        trendingBoundaryCallback = TrendingBoundaryCallback(application, mockLiveData)
    }

    @Test
    fun testPaginationThenRefresh() {

        verifyZeroInteractions(mockLiveData)

        trendingBoundaryCallback.onZeroItemsLoaded()
        verify(mockLiveData).postValue(eq(true))
        verify(mockLiveData).postValue(eq(false))
        verifyNoMoreInteractions(mockLiveData)
        verify(dataController).fetchTrending(eq(0))

        trendingBoundaryCallback.onItemAtEndLoaded(fakeGif)
        verify(mockLiveData, times(2)).postValue(eq(false))
        verifyNoMoreInteractions(mockLiveData)
        verify(dataController).fetchTrending(eq(10))

        trendingBoundaryCallback.onZeroItemsLoaded()
        verify(mockLiveData, times(2)).postValue(eq(true))
        verify(mockLiveData, times(3)).postValue(eq(false))
        verifyNoMoreInteractions(mockLiveData)
        verify(dataController, times(2)).fetchTrending(eq(0))
    }
}