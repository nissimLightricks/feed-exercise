package com.lightricks.feedexercise.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * todo: add the abstract class that extents RoomDatabase here
 */
@Database(entities = [ItemEntity::class], version = 1)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDAO
}