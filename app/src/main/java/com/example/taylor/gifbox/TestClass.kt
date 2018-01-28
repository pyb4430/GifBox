package com.example.taylor.gifbox

import android.app.Application
import javax.inject.Inject

/**
 * Created by Taylor on 1/27/2018.
 */
class TestClass(application: Application) {

    @Inject
    lateinit var application: Application
}