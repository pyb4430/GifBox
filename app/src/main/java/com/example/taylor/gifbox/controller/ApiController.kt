package com.example.taylor.gifbox.controller

import com.example.taylor.gifbox.GiphyApi
import com.example.taylor.gifbox.response.GifListResponse
import io.reactivex.Observable

/**
 * Created by Taylor on 1/28/2018.
 */
class ApiControllerImpl(private val api: GiphyApi, private val apiKey: String): ApiController {

    override fun fetchTrending(limit: Int, rating: String, offset: Int): Observable<GifListResponse> {
        return api.fetchTrending(apiKey, limit, rating, offset)
    }
}

interface ApiController {
    fun fetchTrending(limit: Int, rating: String, offset:Int): Observable<GifListResponse>
}