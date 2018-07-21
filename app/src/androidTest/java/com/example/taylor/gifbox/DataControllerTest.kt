package com.example.taylor.gifbox

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.taylor.gifbox.controller.ApiController
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.controller.DataControllerImpl
import com.example.taylor.gifbox.module.GifBoxDatabase
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.*
import org.junit.runner.RunWith

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.ClassRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class DataControllerTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()
        @ClassRule @JvmField
        val instantTaskExecutorRule = InstantTaskExecutorRule()
    }

    lateinit var db: GifBoxDatabase
    lateinit var dataController: DataController

    lateinit var mockApiController: ApiController

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), GifBoxDatabase::class.java).allowMainThreadQueries().build()
        mockApiController = Mockito.mock(ApiController::class.java)
        dataController = DataControllerImpl(db, mockApiController)

        //TODO: Use getObjFromJsonFile instead
        val gifListResponse = getGifListResponse()
        whenever(mockApiController.fetchTrending(anyInt(), anyString(), anyInt())).thenReturn(Observable.just(gifListResponse))
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testFetchTrending() {
        val gifLiveData = dataController.getAllGifs()
        var allGifLiveData = gifLiveData.getValueBlocking()
        Assert.assertEquals(0, allGifLiveData?.size)

        dataController.fetchTrending()

        allGifLiveData = gifLiveData.getValueBlocking()
        Assert.assertEquals(1, allGifLiveData?.size)
    }
}