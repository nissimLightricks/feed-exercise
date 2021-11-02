package com.lightricks.feedexercise.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
abstract class FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(items: List<ItemEntity>)

    @Delete
    abstract fun deleteAsync(item: ItemEntity): Completable

    @Query("DELETE FROM items")
    abstract fun deleteAll()

    @Query("SELECT * FROM items")
    abstract fun getAll(): Observable<List<ItemEntity>>

    @Query("SELECT count(*) FROM items")
    abstract fun count(): Single<Int>

    @Transaction
    open fun refreshDatabase(items: List<ItemEntity>){
        deleteAll()
        insertAll(items)
    }

    fun refreshDatabaseAsync(items: List<ItemEntity>) :Completable{
        return Completable.fromAction{ refreshDatabase(items) }
    }

}
