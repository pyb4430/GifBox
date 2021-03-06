package com.example.taylor.gifbox

import com.example.taylor.gifbox.response.GifListResponse
import retrofit2.Callback
import javax.inject.Inject

/**
 * Created by Taylor on 1/28/2018.
 */
class ApiController(val api: GiphyApi, val apiKey: String) {

    fun fetchTrending(limit: Int, rating: String, callback: Callback<GifListResponse>) {
        val call = api.fetchTrending(apiKey, limit, rating)
        call.enqueue(callback)
    }
}