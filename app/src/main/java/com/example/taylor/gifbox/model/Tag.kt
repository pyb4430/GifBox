package com.example.taylor.gifbox.model

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.IGNORE


@Entity
class Tag(@PrimaryKey val id: String) {
    companion object {
        // List of tags to prepopulate the database with
        const val TRENDING_TAG = "trending"
        val TAGS = arrayOf(Tag(TRENDING_TAG))
    }
}

@Dao
interface TagDao {
    @Insert(onConflict = IGNORE)
    fun insertAll(vararg tags: Tag)
}