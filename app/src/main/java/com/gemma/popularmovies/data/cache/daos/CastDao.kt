package com.gemma.popularmovies.data.cache.daos

import androidx.room.*
import com.gemma.popularmovies.data.cache.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CastDao {

    @Transaction
    @Query("SELECT * FROM role WHERE movie_id= :movieId")
    fun getMovieCast(movieId:Int): Flow<List<CachedRoleAggregate?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artist: List<CachedArtist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoles(role: List<CachedRole>)
}