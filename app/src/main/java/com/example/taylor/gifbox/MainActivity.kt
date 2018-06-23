package com.example.taylor.gifbox

import android.arch.lifecycle.Observer
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.model.Gif
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPrefs: SharedPreferences

    @Inject
    lateinit var dataController: DataController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as GifBoxApplication).appComponent.inject(this)

        val gifs = dataController.getAllGifs()

        gifs.observe(this, object : Observer<List<Gif>> {
            override fun onChanged(t: List<Gif>?) {
                Timber.d("gif list: ${t}")
            }
        })

        dataController.fetchTrending()
    }
}
