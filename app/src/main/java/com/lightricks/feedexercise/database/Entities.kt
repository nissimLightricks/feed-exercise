package com.lightricks.feedexercise.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * todo: add Room's Entity data class(es) here
 */

@Entity(tableName = "cache")
data class CacheData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "thumbnail_url")
    val thumbnail_url: String,
    @ColumnInfo(name = "is_premium")
    val is_premium: Boolean
)
