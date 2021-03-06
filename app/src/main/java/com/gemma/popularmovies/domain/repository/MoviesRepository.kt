package com.gemma.popularmovies.domain.repository

import androidx.paging.PagingData
import com.gemma.popularmovies.data.MoviesRepositoryImpl
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Provider
import com.gemma.popularmovies.domain.model.Role
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface MoviesRepository {
    suspend fun getMostPopularMovies(): Flow<PagingData<Movie>>
    suspend fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun getMovieById(movieId: Int): Flow<Movie?>
    suspend fun toggleFavorite(movieId: Int)
    suspend fun reloadMovies()
    suspend fun getMovieCast(movieId: Int): Flow<List<Role?>>
    suspend fun getMovieProviders(movieId: Int): Flow<List<Provider?>>
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DependenciesBindings {

    @Singleton
    @Binds
    abstract fun bindRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository
}
