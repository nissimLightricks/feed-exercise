package com.lightricks.feedexercise.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Single

/***
 * todo: add Room's Data Access Object interface(s) here
 */
@Dao
interface ItemsDataAccessObject{
    @Insert
    fun insertAll(entity :ItemEntity):Completable

    @Delete
    fun deleteAll():Completable

    @Query("SELECT * FROM ItemEntity")
    fun queryAll():Observable<List<ItemEntity>>

    @Query("SELECT count(*) FROM ItemEntity")
    fun count(): Single<Int>
}