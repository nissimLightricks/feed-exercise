package com.lightricks.feedexercise.network

/**
 * todo: add Data Transfer Object data class(es) here
 */

data class TemplatesMetadataItem(
    val configuration : String,
    val id : String,
    val isNew : Boolean,
    val isPremium : Boolean,
    val templateCategories : List<String>,
    val templateName : String,
    val templateThumbnailURI : String
)

data class TemplatesMetadata(
    val templatesMetadata : List<TemplatesMetadataItem>
)