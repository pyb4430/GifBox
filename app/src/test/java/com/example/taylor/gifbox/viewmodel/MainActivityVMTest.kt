package com.example.taylor.gifbox.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.paging.DataSource
import com.example.taylor.gifbox.GifBoxApplication
import com.example.taylor.gifbox.TestAppComponent
import com.example.taylor.gifbox.model.Gif
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityVMTest {

    companion object {
        @ClassRule
        @JvmField
        val rule = InstantTaskExecutorRule()
    }

    val testAppComponent = TestAppComponent()

    @Mock
    lateinit var application: GifBoxApplication
    lateinit var viewModel: MainActivityVM

    @Mock
    lateinit var mockDataSourceFactory: DataSource.Factory<Int, Gif>

    @Before
    fun setUp() {
        application.appComponent = testAppComponent

        whenever(testAppComponent.dataController.getAllGifsPaginated()).thenReturn(mockDataSourceFactory)

        viewModel = MainActivityVM(application)
    }

    @Test
    fun testTrendingGifPagedList_notNull() {
        assertNotNull(viewModel.trendingGifPagedList)
    }

    @Test
    fun testRefreshTrending() {
        viewModel.refreshTrending()
        verify(testAppComponent.dataController).clearGifTags(eq("trending"))
    }

    @Test
    fun testFirstPageLoading() {
        assertEquals(viewModel.firstPageLoading.value, false)
    }
}