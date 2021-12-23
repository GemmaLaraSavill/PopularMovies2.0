package com.gemma.popularmovies.data

import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Provider
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

class EmptyDataSource: MovieDataSource {

    private var fakeData = FakeData()
    val fakeMovieList = mutableListOf<Movie>()

    override suspend fun getPopularMovies(): Flow<List<Movie>> {
        return flow {
            emit(emptyList<Movie>())
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return emptyFlow()
    }

    override suspend fun insertFreshPopularMovies(popularMovies: List<Movie>) {
        // simulates getting data from network
        fakeMovieList.addAll(fakeData.getMovieList())
    }

    override suspend fun getFreshPopularMovies(): List<Movie> {
        return emptyList()
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        return emptyFlow()
    }

    override suspend fun insertMovie(fullMovieData: Flow<Movie?>) {
        // not necessary as it is an empty data source
    }

    override suspend fun toggleFavorite(movieId: Int) {
        // not necessary as it is an empty data source
    }

    override suspend fun getTrailer(movieId: Int): Flow<Trailer?> {
        return emptyFlow()
    }

    override suspend fun insertTrailer(movieId: Int, trailer: Trailer?) {
        // not necessary as it is an empty data source
    }

    override suspend fun getFreshMovieCast(movieId: Int): List<Role?> {
        return emptyList()
    }

    override suspend fun getMovieCast(movieId: Int): Flow<List<Role?>> {
        return emptyFlow()
    }

    override suspend fun insertCast(roleList: List<Role?>) {
        // not necessary as it is an empty data source
    }

    override suspend fun getProviders(movieId: Int): Flow<List<Provider?>> {
        return emptyFlow()
    }

    override suspend fun getFreshProviders(movieId: Int): List<Provider?> {
        return emptyList()
    }

    override suspend fun insertProviders(providerList: List<Provider?>) {
        // not necessary as it is an empty data source
    }


}