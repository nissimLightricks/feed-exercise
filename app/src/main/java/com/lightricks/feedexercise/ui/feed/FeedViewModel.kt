package com.lightricks.feedexercise.ui.feed

import androidx.lifecycle.*
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedItemDtoList
import com.lightricks.feedexercise.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalArgumentException

/**
 * This view model manages the data for [FeedFragment].
 */
open class FeedViewModel : ViewModel() {
    private val stateInternal: MutableLiveData<State> = MutableLiveData<State>(DEFAULT_STATE)
    private val networkErrorEvent = MutableLiveData<Event<String>>()

    private var refreshTaskDisposable: Disposable = Disposables.empty()

    fun getIsLoading(): LiveData<Boolean> {
        return Transformations.map(stateInternal) { it.isLoading }
    }

    fun getIsEmpty(): LiveData<Boolean> {
        return Transformations.map(stateInternal) { it.feedItems?.isEmpty() ?: true }
    }

    fun getFeedItems(): LiveData<List<FeedItem>> {
        return Transformations.map(stateInternal) { it.feedItems ?: emptyList() }
    }

    fun getNetworkErrorEvent(): LiveData<Event<String>> = networkErrorEvent

    init {
        refresh()
    }

    fun refresh() {
        if (getState().isLoading) {
            return
        }
        updateState { copy(isLoading = true) }
        refreshTaskDisposable = FeedApiService.instance.getFeed()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ feedResponse ->
                updateState {
                    copy(
                        feedItems = feedResponse.templatesMetadata.toFeedItems(),
                        isLoading = false
                    )
                }
            }, { error ->
                updateState { copy(isLoading = false) }
                networkErrorEvent.postValue(Event(error.message.toString()))
            })
    }

    private fun List<FeedItemDtoList.Item>.toFeedItems(): List<FeedItem> {
        return map { item: FeedItemDtoList.Item ->
            FeedItem(
                item.id,
                FeedApiService.IMAGE_BASE_URL + item.templateThumbnailURI,
                item.isPremium
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        refreshTaskDisposable.dispose()
    }

    private fun updateState(transform: State.() -> State) {
        stateInternal.value = transform(getState())
    }

    private fun getState(): State {
        return stateInternal.value!!
    }

    data class State(
        val feedItems: List<FeedItem>?,
        val isLoading: Boolean
    )

    companion object {
        private val DEFAULT_STATE = State(
            feedItems = null,
            isLoading = false
        )

    }
}

/**
 * This class creates instances of [FeedViewModel].
 * It's not necessary to use this factory at this stage. But if we will need to inject
 * dependencies into [FeedViewModel] in the future, then this is the place to do it.
 */
class FeedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            throw IllegalArgumentException("factory used with a wrong class")
        }
        @Suppress("UNCHECKED_CAST")
        return FeedViewModel() as T
    }
}
