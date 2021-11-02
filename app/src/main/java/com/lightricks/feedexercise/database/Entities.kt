package com.lightricks.feedexercise.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    val configuration: String,
    @PrimaryKey val id: String,
    val isNew: Boolean,
    val isPremium: Boolean,
    val templateName: String,
    val templateThumbnailURI: String
)
