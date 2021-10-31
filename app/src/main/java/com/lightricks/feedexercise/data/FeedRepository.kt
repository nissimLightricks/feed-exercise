package com.lightricks.feedexercise.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedMetadataEntity
import com.lightricks.feedexercise.network.FeedItemEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
class FeedRepository {
    //todo: implement
    private val feedLiveData = MutableLiveData(emptyList<FeedItem>())
    private val singleFeed: Single<FeedMetadataEntity> = FeedApiService.feedApiService.getFeed()

    fun refresh() {
        singleFeed.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ feedResponse ->
                feedLiveData.value = feedResponse.templatesMetadata.toFeedItems()
            }, { error -> })
    }

    fun getFeed(): LiveData<List<FeedItem>> {
        return feedLiveData
    }
}

fun List<FeedItemEntity>.toFeedItems(): List<FeedItem> {
    val prefix = "http://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"
    return map {
        FeedItem(it.id, prefix + it.templateThumbnailURI, it.isPremium)
    }
}
