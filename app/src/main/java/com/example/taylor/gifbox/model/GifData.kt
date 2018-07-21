package com.example.taylor.gifbox.model

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.squareup.moshi.Json
import timber.log.Timber

/**
 * Created by Taylor on 1/28/2018.
 */
@Entity(tableName = "gif")
data class Gif(@PrimaryKey val id: String,
               val url: String,
               @Embedded @Json(name = "images") val gifImageData: GifImageData)

data class GifImageData(@Embedded(prefix = "original_still_") @Json(name = "original_still") val originalStill: GifData?,
                        @Embedded(prefix = "preview_") val preview: GifData?,
                        @Embedded(prefix = "fixed_width_small_still_") @Json(name = "fixed_width_small_still") val fixedWidthSmallStill: GifData?,
                        @Embedded(prefix = "fixed_width_small_") @Json(name = "fixed_width_small") val fixedWidthSmall: GifData?)

data class GifData(val width: Int?,
                   val height: Int?,
                   val size: Long?,
                   val url: String?,
                   val mp4: String?,
                   @Json(name = "mp4_size") val mp4Size: Long?)


@Dao
interface GifDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(vararg gif: Gif)

    @Query("SELECT * FROM gif INNER JOIN gif_tag_join ON gif.id=gif_tag_join.gifId WHERE tagId == '${Tag.TRENDING}' ORDER BY position")
    fun getAllPaginated(): DataSource.Factory<Int, Gif>

    @Query("SELECT * FROM gif")
    fun getAll(): LiveData<List<Gif>>

    @Query("SELECT * FROM gif INNER JOIN gif_tag_join WHERE tagId == '${Tag.TRENDING}' ORDER BY position")
    fun getAllNow(): List<Gif>
}


@Entity(tableName = "gif_tag_join",
        primaryKeys = [ "gifId", "tagId" ],
        foreignKeys = [
            ForeignKey(entity = Gif::class,
                    parentColumns = ["id"],
                    childColumns = ["gifId"]),
            ForeignKey(entity = Tag::class,
                    parentColumns = ["id"],
                    childColumns = ["tagId"])
        ],
        indices = [Index("tagId")])
class GifTagJoin(val gifId: String, val tagId: String, val position: Int = 0)

@Dao
abstract class GifTagDao {

    @Insert(onConflict = REPLACE)
    abstract fun insertAll(vararg gifTagJoin: GifTagJoin)

    fun tagGifs(tag: String, offset: Int, vararg gifs: Gif) {
        val gifTagJoins = gifs.mapIndexed { index, gif ->
            GifTagJoin(gif.id, tag, offset + index)
        }
        gifTagJoins.forEachIndexed { index, gifTagJoin ->
            Timber.d("heres tag $index: ${gifTagJoin.gifId} ${gifTagJoin.position}")
        }
        insertAll(*gifTagJoins.toTypedArray())
    }

    @Query("DELETE FROM gif_tag_join WHERE tagId = :tag")
    abstract fun clearTag(tag: String)
}