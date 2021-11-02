package com.lightricks.feedexercise.ui.feed

import androidx.lifecycle.*
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.room.Room
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.data.FeedRepository
import com.lightricks.feedexercise.util.Event
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.internal.operators.completable.CompletableError
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalArgumentException
import kotlin.coroutines.coroutineContext

/**
 * This view model manages the data for [FeedFragment].
 */
open class FeedViewModel() : ViewModel() {
    private val stateInternal: MutableLiveData<State> = MutableLiveData<State>(DEFAULT_STATE)
    private val networkErrorEvent = MutableLiveData<Event<String>>()
    private val feedRepository = FeedRepository()

    fun getIsLoading(): LiveData<Boolean> {
        //todo: fix the implementation
        return MutableLiveData(getState().isLoading)
    }

    fun getIsEmpty(): LiveData<Boolean> {
        //todo: fix the implementation
        return MutableLiveData(getState().feedItems?.isEmpty())
    }

    fun getFeedItems(): LiveData<List<FeedItem>> {
        //todo: fix the implementation
        return feedRepository.getFeed()
    }

    fun getNetworkErrorEvent(): LiveData<Event<String>> = networkErrorEvent

    init {
        refresh()
    }

    fun refresh() {
        updateState { State(feedItems = feedItems, isLoading = true) }
        feedRepository.refresh()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateState {
                        State(feedItems = feedRepository.getFeed().value, isLoading = false)
                    }
                },
                { error ->
                    networkErrorEvent.value = Event(error.toString())
                })
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
