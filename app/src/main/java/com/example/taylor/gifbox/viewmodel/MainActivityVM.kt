package com.example.taylor.gifbox.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.example.taylor.gifbox.GifBoxApplication
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.model.Gif
import javax.inject.Inject

class MainActivityVM(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var dataController: DataController

    val trendingGifs: LiveData<List<Gif>>

    init {
        (application as GifBoxApplication).appComponent.inject(this)

        trendingGifs = dataController.getAllGifs()
    }

    fun fetchTrending() {
        dataController.fetchTrending()
    }
}