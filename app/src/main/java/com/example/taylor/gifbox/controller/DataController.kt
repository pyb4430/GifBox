package com.example.taylor.gifbox.controller

import android.arch.lifecycle.LiveData
import com.example.taylor.gifbox.model.*
import com.example.taylor.gifbox.response.GifListResponse
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Taylor on 1/29/2018.
 */
class DataController(val db: BoxStore, val apiController: ApiController) {

    // api calls

    fun fetchTrending() {
        apiController.fetchTrending(20, "R", object : Callback<GifListResponse> {
            override fun onResponse(call: Call<GifListResponse>?, response: Response<GifListResponse>?) {
                response?.body()?.gifList?.let {
                    writeGifMetaDatas(it)
                }
            }

            override fun onFailure(call: Call<GifListResponse>?, t: Throwable?) {

            }
        })
    }


    // db writes

    fun writeGifMetaDatas(gifs: List<Gif>) {
        val gifBox = db.boxFor(Gif::class.java)
        val gifImageDataBox = db.boxFor(GifImageData::class.java)
        val gifDataBox = db.boxFor(GifData::class.java)
        gifs.forEach {
            val ob = GifOb(url = it.url)
            val gifImageDataOb = GifImageDataOb()
            gifImageDataOb = 
            ob.gifImageData.setAndPutTarget(it.gifImageData)
            ob.gifImageData.
            gifDataBox.put(it.gifImageData.fixedWidthSmall,
                    it.gifImageData.fixedWidthSmallStill,
                    it.gifImageData.originalStill,
                    it.gifImageData.preview)
        }
        val ob = GifOb()
        db.boxFor(Gif::class.java).
    }


    // data gets

    fun getAllGifs(): LiveData<List<Gif>> {
        return ObjectBoxLiveData<Gif>(db.boxFor(Gif::class.java).query().build())
    }

}