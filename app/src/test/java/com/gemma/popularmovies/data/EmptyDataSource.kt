package com.gemma.popularmovies.data

import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EmptyDataSource: MovieDataSource {

    val fakeMovieList = mutableListOf<Movie>()

    override suspend fun getPopularMovies(): Flow<List<Movie>> {
        return flow {
            emit(emptyList<Movie>())
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFreshPopularMovies(popularMovies: List<Movie>) {
        fakeMovieList.addAll(popularMovies)
    }

    override suspend fun getFreshPopularMovies(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovie(fullMovieData: Flow<Movie?>) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(movieId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTrailer(movieId: Int): Flow<Trailer?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTrailer(movieId: Int, trailer: Trailer?) {
        TODO("Not yet implemented")
    }


}