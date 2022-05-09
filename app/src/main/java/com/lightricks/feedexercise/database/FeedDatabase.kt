package com.lightricks.feedexercise.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

/**
 * todo: add the abstract class that extents RoomDatabase here
 */

@Database(entities = [CacheData::class], version = 1)
abstract class FeedDatabase: RoomDatabase() {

    abstract fun cacheDao(): CacheDAO

    companion object {
        private var INSTANCE: FeedDatabase? = null

        fun getDatabase(context: Context): FeedDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext, FeedDatabase::class.java, "cache")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}