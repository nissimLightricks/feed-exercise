package com.lightricks.feedexercise.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.lightricks.feedexercise.FeedApplication
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.database.ItemEntity
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedMetadataEntity
import com.lightricks.feedexercise.network.FeedItemEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.CompletableSubject
import java.util.concurrent.Callable

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
class FeedRepository {
    //todo: implement
    private val roomDB = Room.databaseBuilder(
        FeedApplication.context,
        FeedDatabase::class.java,
        "database")
        .build()
    private val roomDao = roomDB.itemDao()

    private val feedLiveData: LiveData<List<FeedItem>> =
        LiveDataReactiveStreams.fromPublisher(
            roomDao.getAll().map {
                it.toFeedItemsFromEntities()
            }.toFlowable(BackpressureStrategy.LATEST))

    fun refresh(): Completable {
        return FeedApiService.feedApiService.getFeed()
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { feedResponse ->
                roomDao.insertAll(feedResponse.templatesMetadata.toFeedEntities())
            }
    }

    fun getFeed(): LiveData<List<FeedItem>> {
        return feedLiveData
    }
}


fun List<ItemEntity>.toFeedItemsFromEntities(): List<FeedItem> {
    val prefix = "http://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"
    return map {
        FeedItem(it.id, prefix + it.templateThumbnailURI, it.isPremium)
    }
}

fun List<FeedItemEntity>.toFeedEntities(): List<ItemEntity> {
    return map {
        ItemEntity(
            configuration = it.configuration,
            id = it.id,
            isNew = it.isNew,
            isPremium = it.isPremium,
            templateName = it.templateName,
            templateThumbnailURI = it.templateThumbnailURI,
        )
    }
}
