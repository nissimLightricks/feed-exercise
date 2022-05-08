package com.lightricks.feedexercise.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


/**
 * todo: add the FeedApiService interface and the Retrofit and Moshi code here
 */


interface FeedApiService {
    @GET("Android/demo/feed.json")
    fun getFeed():  Single<FeedItemDtoList>

    companion object {
        private const val BASE_URL = "https://assets.swishvideoapp.com/"
        const val IMAGE_BASE_URL = BASE_URL + "Android/demo/catalog/thumbnails/"
        private val retrofit: Retrofit = run {
            val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        val instance: FeedApiService = retrofit.create(FeedApiService::class.java)
    }
}


