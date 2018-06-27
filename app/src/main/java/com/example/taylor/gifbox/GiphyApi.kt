package com.example.taylor.gifbox

import com.example.taylor.gifbox.response.GifListResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Taylor on 1/28/2018.
 */
interface GiphyApi {

    @GET("gifs/trending")
    fun fetchTrending(@Query("api_key") apiKey: String,
                      @Query("limit") limit : Int,
                      @Query("rating") rating: String) : Observable<GifListResponse>
}