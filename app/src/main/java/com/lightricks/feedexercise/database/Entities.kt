package com.lightricks.feedexercise.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * todo: add Room's Entity data class(es) here
 */
@Entity
data class AllItemsEntity(
    @ColumnInfo val templatesMetadata:List<ItemEntity>
)

@Entity
data class ItemEntity(
    @ColumnInfo val configuration: String,
    @PrimaryKey val id: String,
    @ColumnInfo val isNew: Boolean,
    @ColumnInfo val isPremium: Boolean,
    @ColumnInfo val templateCategories: List<String>,
    @ColumnInfo val templateName: String,
    @ColumnInfo val templateThumbnailURI: String
)