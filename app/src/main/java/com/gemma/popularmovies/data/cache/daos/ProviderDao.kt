package com.gemma.popularmovies.data.cache.daos

import androidx.room.*
import com.gemma.popularmovies.data.cache.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {

    @Transaction
    @Query("SELECT * FROM movie_provider WHERE movie_id= :movieId")
    fun getMovieProviders(movieId:Int): Flow<List<CachedProviderAggregate?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProviders(artist: List<CachedProvider>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvidersForMovie(role: List<CachedProviderForMovie>)
}