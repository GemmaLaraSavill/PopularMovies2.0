package com.gemma.popularmovies.data

import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow

interface MovieDataSource {

    suspend fun getPopularMovies(): Flow<List<Movie>>

    fun getFavoriteMovies(): Flow<List<Movie>>

    suspend fun insertFreshPopularMovies(popularMovies: List<Movie>)

    suspend fun getFreshPopularMovies(): List<Movie>

    suspend fun getMovieById(movieId: Int): Flow<Movie?>

    suspend fun insertMovie(fullMovieData: Flow<Movie?>)

    suspend fun toggleFavorite(movieId: Int)

    suspend fun getTrailer(movieId: Int): Flow<Trailer?>

    suspend fun insertTrailer(movieId: Int, trailer:Trailer?)

    suspend fun getFreshMovieCast(movieId: Int): List<Role?>

    suspend fun getMovieCast(movieId: Int): Flow<List<Role?>>

    suspend fun insertCast(roleList: List<Role?>)
}