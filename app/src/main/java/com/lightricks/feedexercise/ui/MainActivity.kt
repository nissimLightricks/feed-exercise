package com.lightricks.feedexercise.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.lightricks.feedexercise.R
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.network.FeedApiResponseGenerator
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.TemplatesMetadata
import com.lightricks.feedexercise.network.TemplatesMetadataItem
import com.lightricks.feedexercise.ui.feed.FeedFragment
import com.lightricks.feedexercise.ui.feed.FeedViewModel
import com.lightricks.feedexercise.ui.feed.FeedViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * This is the main entry point into the app. This activity shows the [FeedFragment].
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view,FeedFragment()).commit()
    }



}