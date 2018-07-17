package com.example.taylor.gifbox.viewmodel

import android.arch.lifecycle.LiveData
import com.example.taylor.gifbox.GifBoxApplication
import com.example.taylor.gifbox.TestAppComponent
import com.example.taylor.gifbox.model.Gif
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityVMTest {

    val testAppComponent = TestAppComponent()

    @Mock
    lateinit var application: GifBoxApplication
    lateinit var viewModel: MainActivityVM

    @Mock
    lateinit var mockLiveData: LiveData<List<Gif>>

    @Before
    fun setUp() {
        application.appComponent = testAppComponent

        whenever(testAppComponent.dataController.getAllGifs()).thenReturn(mockLiveData)

        viewModel = MainActivityVM(application)
    }

    @Test
    fun testTrendingGifs() {
        assertEquals(mockLiveData, viewModel.trendingGifPagedList)
    }

    @Test
    fun testFetchTrendingGifs() {
        viewModel.refreshTrending()
        verify(testAppComponent.dataController).fetchTrending()
    }
}