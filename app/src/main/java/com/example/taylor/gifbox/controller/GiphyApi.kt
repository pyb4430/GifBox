package com.example.taylor.gifbox.controller

import com.example.taylor.gifbox.response.GifListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Taylor on 1/28/2018.
 */

interface GiphyApi {

    @GET("gifs/trending")
    fun fetchTrending(@Query("api_key") apiKey: String,
                      @Query("limit") limit : Int = 10,
                      @Query("rating") rating: String = "R",
                      @Query("offset") offset: Int = 0) : Observable<GifListResponse>
}