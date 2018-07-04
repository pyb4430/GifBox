package com.example.taylor.gifbox

import com.example.taylor.gifbox.response.GifListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Taylor on 1/28/2018.
 */
const val TRENDING_URL = "gifs/trending"

interface GiphyApi {

    @GET(TRENDING_URL)
    fun fetchTrending(@Query("api_key") apiKey: String,
                      @Query("limit") limit : Int = 10,
                      @Query("rating") rating: String = "R",
                      @Query("offset") offset: Int = 0) : Observable<GifListResponse>
}