package com.example.taylor.gifbox.controller

import android.arch.lifecycle.LiveData
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.module.GifBoxDatabase
import com.example.taylor.gifbox.response.GifListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Taylor on 1/29/2018.
 */
class DataController(val db: GifBoxDatabase, val apiController: ApiController) {

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
        db.gifDao().insertAll(*gifs.toTypedArray())
    }


    // data gets

    fun getAllGifs(): LiveData<List<Gif>> {
        return db.gifDao().getAll()
    }

}