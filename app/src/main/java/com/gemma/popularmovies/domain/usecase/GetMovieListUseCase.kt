package com.gemma.popularmovies.domain.usecase

import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(private val popularMoviesRepository: MoviesRepository) {

    suspend fun getMostPopularMovies(): Flow<List<Movie>> {
        return popularMoviesRepository.getMostPopularMovies()
    }

    suspend fun getFavoriteMovies(): Flow<List<Movie>> {
        return popularMoviesRepository.getFavoriteMovies()
    }

}