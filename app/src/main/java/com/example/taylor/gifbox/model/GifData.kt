package com.example.taylor.gifbox.model

import com.squareup.moshi.Json
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

/**
 * Created by Taylor on 1/28/2018.
 */

data class Gif(val id: Long,
               val url: String,
               @Json(name = "images") val gifImageData: GifImageData)


data class GifImageData(@Json(name = "original_still") val originalStill: GifData,
                        val preview: GifData,
                        @Json(name = "fixed_width_small_still") val fixedWidthSmallStill: GifData,
                        @Json(name = "fixed_width_small") val fixedWidthSmall: GifData)

data class GifData(val width: Int,
                   val height: Int,
                   val size: Long?,
                   val url: String?,
                   val mp4: String?,
                   @Json(name = "mp4_size") val mp4Size: Long?)

@Entity
data class GifOb(@Id var id: Long = 0,
               val url: String) {
    @Json(name = "images") lateinit var gifImageData: ToOne<GifImageDataOb>
}

@Entity
data class GifImageDataOb(@Id var id: Long = 0) {
    @Json(name = "original_still") lateinit var originalStill: ToOne<GifDataOb>
    lateinit var preview: ToOne<GifDataOb>
    @Json(name = "fixed_width_small_still") lateinit var fixedWidthSmallStill: ToOne<GifDataOb>
    @Json(name = "fixed_width_small") lateinit var fixedWidthSmall: ToOne<GifDataOb>
}

@Entity
data class GifDataOb(@Id var id: Long = 0,
                   val width: Int,
                   val height: Int,
                   val size: Long?,
                   val url: String?,
                   val mp4: String?,
                   @Json(name = "mp4_size") val mp4Size: Long?)