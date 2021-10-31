package com.lightricks.feedexercise.network

import com.lightricks.feedexercise.data.FeedItem

/**
 * todo: add Data Transfer Object data class(es) here
 */

data class FeedMetadataEntity(
    val templatesMetadata:List<FeedItemEntity>
)

data class FeedItemEntity(
    val configuration:String,
    val id: String,
    val isNew: Boolean,
    val isPremium: Boolean,
    val templateCategories: List<String>,
    val templateName:String,
    val templateThumbnailURI:String
)
