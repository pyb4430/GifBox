package com.example.taylor.gifbox.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.taylor.gifbox.R
import com.example.taylor.gifbox.adapter.GifAdapter
import com.example.taylor.gifbox.viewmodel.MainActivityVM
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityVM

    val adapter = GifAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityVM::class.java)
        viewModel.trendingGifPagedList.observe(this, Observer { adapter.submitList(it)})

        feedRecyclerView.adapter = adapter
        feedRecyclerView.layoutManager = LinearLayoutManager(this)

        feedSwipeRefresh.setOnRefreshListener {
            viewModel.refreshTrending()
        }

        viewModel.firstPageLoading.observe(this, Observer {
            feedSwipeRefresh.isRefreshing = it == true
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.refreshTrending()
    }
}