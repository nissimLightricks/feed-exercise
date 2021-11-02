package com.lightricks.feedexercise.data

import android.view.SurfaceControl
import androidx.room.Transaction
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.ItemEntity
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedItemEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
class FeedRepository(
    private val feedApiService: FeedApiService,
    private val feedDao: FeedDao) {

    fun getFeed(): Observable<List<FeedItem>> {
        return feedDao.getAll().map { it.toFeedItems() }
    }

    fun refresh(): Completable {
        return feedApiService.getFeed()
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { feedResponse ->
                feedDao.refreshDatabaseAsync(feedResponse.templatesMetadata.toFeedEntities())
            }
    }
}

fun List<ItemEntity>.toFeedItems(): List<FeedItem> {
    val prefix = "http://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"
    return map { FeedItem(it.id, prefix + it.templateThumbnailURI, it.isPremium) }
}

fun List<FeedItemEntity>.toFeedEntities(): List<ItemEntity> {
    return map {
        ItemEntity(
            configuration = it.configuration,
            id = it.id,
            isNew = it.isNew,
            isPremium = it.isPremium,
            templateName = it.templateName,
            templateThumbnailURI = it.templateThumbnailURI)
    }
}
