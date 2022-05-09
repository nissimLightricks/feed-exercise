package com.lightricks.feedexercise.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/***
 * todo: add Room's Data Access Object interface(s) here
 */

@Dao
interface CacheDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(cacheItems: List<CacheData>): Completable

    @Query("DELETE FROM cache")
    fun deleteAll(): Completable

    @Query("SELECT * FROM cache")
    fun readAllData(): Observable<List<CacheData>>

    @Query("SELECT COUNT(*) FROM cache")
    fun getNumberOfEntities(): Single<Int>
}
