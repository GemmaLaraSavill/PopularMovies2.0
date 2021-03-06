package com.gemma.popularmovies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.gemma.popularmovies.data.cache.model.CachedMovieMinimal
import com.gemma.popularmovies.data.network.MovieRemoteMediator
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Provider
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow

interface MovieDataSource {

    @ExperimentalPagingApi
    suspend fun getPagedMovies(moviesRemoteMediator: MovieRemoteMediator): Flow<PagingData<CachedMovieMinimal>>

    fun getFavoriteMovies(): Flow<List<Movie>>

    suspend fun refreshMovies(popularMovies: List<Movie>)

    suspend fun getFreshPopularMovies(page: Int): List<Movie>

    suspend fun addFreshPopularMovies(movies: List<Movie>)

    suspend fun countMovies(): Int

    suspend fun getMovieById(movieId: Int): Flow<Movie?>

    suspend fun insertMovie(fullMovieData: Flow<Movie?>)

    suspend fun toggleFavorite(movieId: Int)

    suspend fun getTrailer(movieId: Int): Flow<Trailer?>

    suspend fun insertTrailer(movieId: Int, trailer:Trailer?)

    suspend fun getFreshMovieCast(movieId: Int): List<Role?>

    suspend fun getMovieCast(movieId: Int): Flow<List<Role?>>

    suspend fun insertCast(roleList: List<Role?>)

    suspend fun getProviders(movieId: Int): Flow<List<Provider?>>

    suspend fun getFreshProviders(movieId: Int): List<Provider?>

    suspend fun insertProviders(providerList: List<Provider?>)
}