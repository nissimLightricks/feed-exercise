package com.lightricks.feedexercise.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/***
 * todo: add Room's Data Access Object interface(s) here
 */
@Dao
interface ItemDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<ItemEntity>): Completable

    @Delete
    fun delete(item: ItemEntity):Completable

    @Query("DELETE FROM items")
    fun deleteAll()

    @Query("SELECT * FROM items")
    fun getAll(): Observable<List<ItemEntity>>

    @Query("SELECT count(*) FROM items")
    fun count(): Single<Int>
}