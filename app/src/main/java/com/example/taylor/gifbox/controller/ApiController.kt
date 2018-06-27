package com.example.taylor.gifbox.controller

import com.example.taylor.gifbox.GiphyApi
import com.example.taylor.gifbox.response.GifListResponse
import io.reactivex.Observable
import retrofit2.Response

/**
 * Created by Taylor on 1/28/2018.
 */
class ApiController(private val api: GiphyApi, private val apiKey: String) {

    fun fetchTrending(limit: Int, rating: String): Observable<GifListResponse> {
        return api.fetchTrending(apiKey, limit, rating)
    }
}