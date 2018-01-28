package com.example.taylor.gifbox

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as GifBoxApplication).appComponent.inject(this)

        Log.d("Main" , "prefs ${userPrefs.all}")
    }
}
