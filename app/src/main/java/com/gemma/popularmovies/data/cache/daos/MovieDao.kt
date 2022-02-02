package com.gemma.popularmovies.data.cache.daos

import androidx.paging.PagingSource
import androidx.room.*
import com.gemma.popularmovies.data.cache.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT movies.movie_id, title, poster, favorite, page FROM movies")
    fun getMostPopularMovies(): Flow<List<CachedMovieMinimal>>

    @Query("SELECT movies.movie_id, title, poster, favorite, page FROM movies ORDER BY page ASC, rating DESC ")
    fun getMoviesPagingSource(): PagingSource<Int, CachedMovieMinimal>

    @Query("SELECT * FROM movies WHERE favorite=1 ORDER BY title ASC")
    fun getFavoriteMovies(): Flow<List<CachedMovie>>

    @Transaction
    @Query("SELECT * FROM movies WHERE movie_id= :movieId")
    fun getFullMovieData(movieId:Int): Flow<CachedMovieAggregate?>

    @Transaction
    suspend fun refreshMovies(movies:List<CachedMovie>): List<Long> {
        deleteAllNonFavoriteMovies()
        return insertNewMovies(movies)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: CachedMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewMovies(movies: List<CachedMovie>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrailer(trailer: CachedTrailer)

    @Query("DELETE FROM movies WHERE favorite=0")
    suspend fun deleteAllNonFavoriteMovies()

    @Query("SELECT COUNT(movie_id) FROM  movies")
    suspend fun countMovies(): Int

    @Query("UPDATE movies SET favorite = 1 - favorite WHERE movie_id=:movieId")
    suspend fun toggleFavorite(movieId:Int)

}