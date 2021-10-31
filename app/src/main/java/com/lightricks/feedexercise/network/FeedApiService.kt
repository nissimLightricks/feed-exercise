package com.lightricks.feedexercise.network

import retrofit2.http.GET
import retrofit2.Retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.core.Single
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * todo: add the FeedApiService interface and the Retrofit and Moshi code here
 */
interface FeedApiService {
    @GET(FEED_QUERY)
    fun getFeed(): Single<FeedMetadataEntity>

    companion object {
        private const val FEED_QUERY = "feed.json"
        private const val BASE_URL = "https://assets.swishvideoapp.com/Android/demo/"

        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val feedApiService: FeedApiService = retrofit.create(FeedApiService::class.java)
    }
}
