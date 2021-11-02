package com.lightricks.feedexercise.network

data class FeedMetadataEntity(
    val templatesMetadata: List<FeedItemEntity>
)

data class FeedItemEntity(
    val configuration: String,
    val id: String,
    val isNew: Boolean,
    val isPremium: Boolean,
    val templateCategories: List<String>,
    val templateName: String,
    val templateThumbnailURI: String
)
