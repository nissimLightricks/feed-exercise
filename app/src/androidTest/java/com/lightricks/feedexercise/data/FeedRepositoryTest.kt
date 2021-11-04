package com.lightricks.feedexercise.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedMetadataEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException


@RunWith(MockitoJUnitRunner::class)
class FeedRepositoryTest {
    @get:Rule
    val instantLiveData = InstantTaskExecutorRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var feedDao: FeedDao
    private lateinit var db: FeedDatabase
    private lateinit var repositoryToTest: FeedRepository

    @Mock
    private lateinit var mockApiService: FeedApiService

    @Before
    fun init() {
        RxAndroidPlugins.initMainThreadScheduler { Schedulers.trampoline() }
        createDb()
        createMockApiService()
        repositoryToTest = FeedRepository(mockApiService, feedDao)
    }

    private fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            context, FeedDatabase::class.java
        ).build()
        feedDao = db.feedDao()
    }

    private fun createMockApiService() {
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter(FeedMetadataEntity::class.java)
        val jsonFile = context.assets.open("get_feed_response.json")
        var jsonString = ""
        jsonFile.bufferedReader().forEachLine { jsonString += it }
        val feedMetadataEntity = adapter.fromJson(jsonString)
        `when`(mockApiService.getFeed())
            .thenReturn(Single.just(feedMetadataEntity))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun whenInitCompilesWithoutError_passTest() {
        assert(true)
    }

    @Test
    fun whenRepositoryRefresh_databaseEmpty_willFillDatabaseWithItemsOnTestJson() {
        val actualSize: Int = feedDao.count().blockingGet()
        assertEquals(0, actualSize)

        repositoryToTest.refresh()
            .test()
            .await()
            .assertComplete()
            .assertNoErrors()

        val serverListSingle = mockApiService.getFeed()
            .test()
            .await()
            .assertComplete()
            .assertNoErrors()
        val serverList = serverListSingle.values().first().templatesMetadata.toFeedEntities()
        val databaseListObservable = feedDao.getAll()
            .test()
            .assertNoErrors()
        val databaseList = databaseListObservable.values().first()
        assertEquals(serverList, databaseList)
    }

    @Test
    fun whenGetFeed_databaseEmpty_willReturnEmptyList() {
        val actualSize: Int = feedDao.count().blockingGet()
        assertEquals(0, actualSize)

        val actualListFromRepository = repositoryToTest.getFeed().blockingFirst()
        val expectedList = emptyList<FeedItem>()

        assertEquals(expectedList, actualListFromRepository)
    }
}
