package com.example.taylor.gifbox.response

import com.example.taylor.gifbox.model.Gif
import com.squareup.moshi.Json

/**
 * Created by Taylor on 1/28/2018.
 */
class GifListResponse(@Json(name = "data") val gifList: List<Gif>,
                      val pagination: PaginationObject)

data class PaginationObject(val offset: Int,
                            @Json(name = "total_count") val totalCount: Int,
                            val count: Int)