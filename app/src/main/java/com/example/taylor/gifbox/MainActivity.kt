package com.example.taylor.gifbox

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.viewmodel.MainActivityVM
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityVM

    val adapter = GifAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityVM::class.java)
//        viewModel.trendingGifs.observe(this, Observer<List<Gif>> { t -> Timber.d("gif list: $t") })
        viewModel.trendingGifPagedList.observe(this, Observer { adapter.submitList(it)})

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
//        viewModel.fetchTrending()
    }
}