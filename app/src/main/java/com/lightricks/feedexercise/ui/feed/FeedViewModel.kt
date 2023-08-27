package com.lightricks.feedexercise.ui.feed

import androidx.lifecycle.*
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.network.FeedApiResponseGenerator
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.TemplatesMetadata
import com.lightricks.feedexercise.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalArgumentException

/**
 * This view model manages the data for [FeedFragment].
 */
open class FeedViewModel : ViewModel() {
    init {
        this.fetchFeedItems()
    }


    private val stateInternal: MutableLiveData<State> = MutableLiveData<State>(DEFAULT_STATE)
    private val networkErrorEvent = MutableLiveData<Event<String>>()



    fun getIsLoading(): LiveData<Boolean> {
        return stateInternal.map { state -> state.isLoading }
    }

    fun fetchFeedItems(){

        val feedApiService : FeedApiService = FeedApiResponseGenerator.createFeedApiService()
        feedApiService.getFeedData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ feedResponse ->
                handleResponse(feedResponse)
            }, { error ->
                handleNetworkError(error)
            })
    }

    fun handleNetworkError(error: Throwable){
    }

    fun handleResponse(feedResponse: TemplatesMetadata) {
        val rawResponseList = feedResponse.templatesMetadata
        var feedItemLst : MutableList<FeedItem> = mutableListOf()
        for(item in rawResponseList){
            feedItemLst.add(FeedItem(item.id,"https://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/" + item.templateThumbnailURI,item.isPremium))
        }
        updateState{FeedViewModel.State(feedItemLst,false)}
    }

    fun getIsEmpty(): LiveData<Boolean> {
        //todo: fix the implementation
        return MutableLiveData()
    }

    fun getFeedItems(): LiveData<List<FeedItem>> {
        return stateInternal.map { state -> state.feedItems ?: emptyList() }
    }

    fun getNetworkErrorEvent(): LiveData<Event<String>> = networkErrorEvent

    init {
        refresh()
    }

    fun refresh() {
        //todo: fix the implementation
    }

    private fun updateState(transform: State.() -> State) {
        stateInternal.value = transform(getState())
    }

    private fun getState(): State {
        return stateInternal.value!!
    }

    data class State(
        val feedItems: List<FeedItem>?,
        val isLoading: Boolean)

    companion object {
        private val DEFAULT_STATE = State(
            feedItems = null,
            isLoading = false)
    }
}

/**
 * This class creates instances of [FeedViewModel].
 * It's not necessary to use this factory at this stage. But if we will need to inject
 * dependencies into [FeedViewModel] in the future, then this is the place to do it.
 */
class FeedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            throw IllegalArgumentException("factory used with a wrong class")
        }
        @Suppress("UNCHECKED_CAST")
        return FeedViewModel() as T
    }
}
