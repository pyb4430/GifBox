package com.example.taylor.gifbox.module

import com.example.taylor.gifbox.adapter.TrendingBoundaryCallback
import com.example.taylor.gifbox.viewmodel.MainActivityVM
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Taylor on 1/27/2018.
 */
@Singleton
@Component(modules = [AppModule::class, ApiModule::class, DataModule::class])
interface AppComponent {
    fun inject(mainActivityVM: MainActivityVM)
    fun inject(trendingBoundaryCallback: TrendingBoundaryCallback)
}