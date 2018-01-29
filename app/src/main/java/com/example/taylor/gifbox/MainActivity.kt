package com.example.taylor.gifbox

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.taylor.gifbox.response.GifListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPrefs: SharedPreferences

    @Inject
    lateinit var apiController: ApiController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as GifBoxApplication).appComponent.inject(this)

        Log.d("Main" , "prefs ${userPrefs.all}")

        apiController.fetchTrending(20, "R", object : Callback<GifListResponse> {
            override fun onResponse(call: Call<GifListResponse>?, response: Response<GifListResponse>?) {
                response?.body()?.let {
                    it.gifList?.forEach {
                        Log.d("Main", "gif list url: ${ it.url }")
                    }
                }
            }

            override fun onFailure(call: Call<GifListResponse>?, t: Throwable?) {

            }
        })
    }
}
