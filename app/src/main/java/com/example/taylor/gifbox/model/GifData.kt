package com.example.taylor.gifbox.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.squareup.moshi.Json

/**
 * Created by Taylor on 1/28/2018.
 */
@Entity(tableName = "gif")
data class Gif(@PrimaryKey val id: Long,
               val url: String,
               @Embedded @Json(name = "images") val gifImageData: GifImageData)


data class GifImageData(@Embedded(prefix = "original_still_") @Json(name = "original_still") val originalStill: GifData,
                        @Embedded(prefix = "preview_") val preview: GifData,
                        @Embedded(prefix = "fixed_width_small_still_") @Json(name = "fixed_width_small_still") val fixedWidthSmallStill: GifData,
                        @Embedded(prefix = "fixed_width_small_") @Json(name = "fixed_width_small") val fixedWidthSmall: GifData)

data class GifData(val width: Int,
                   val height: Int,
                   val size: Long?,
                   val url: String?,
                   val mp4: String?,
                   @Json(name = "mp4_size") val mp4Size: Long?)

@Dao
interface GifDao {
    @Insert
    fun insertAll(vararg gif: Gif)

    @Query("SELECT * FROM gif")
    fun getAll(): LiveData<List<Gif>>
}