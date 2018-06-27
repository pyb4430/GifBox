package com.example.taylor.gifbox

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.viewmodel.MainActivityVM
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityVM::class.java)
        viewModel.trendingGifs.observe(this, Observer<List<Gif>> { t -> Timber.d("gif list: $t") })
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchTrending()
    }
}
