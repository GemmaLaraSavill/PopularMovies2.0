package com.gemma.popularmovies.domain.usecase

import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

    suspend fun getMovieDetail(movieId: Int): Flow<Movie?> {
        return moviesRepository.getMovieById(movieId)
    }

    suspend fun toggleFavorite(movieId: Int) {
        moviesRepository.toggleFavorite(movieId)
    }
}