package com.lightricks.feedexercise

import android.app.Application

class FeedApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        context=this
    }

    companion object{
        lateinit var context:FeedApplication
    }
}