package com.example.taylor.gifbox.response

import com.example.taylor.gifbox.model.GifData
import com.squareup.moshi.Json

/**
 * Created by Taylor on 1/28/2018.
 */
class GifListResponse(@Json(name = "data") var gifList: List<GifData>? = null)