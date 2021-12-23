package com.gemma.popularmovies.data

import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Provider
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.model.Trailer
import com.gemma.popularmovies.domain.repository.DataRefreshManager
import com.gemma.popularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Combines local and remote data sources to return movie data
 * Source of truth is the DB cache data
 */
class MoviesRepositoryImpl @Inject constructor(
    @LocalDataSourceModule
    private val movieLocalDataSource: MovieDataSource,
    @NetworkDataSourceModule
    private val movieNetworkDataSource: MovieDataSource,
    private val dataRefreshManagerImpl: DataRefreshManager
) : MoviesRepository {


    override suspend fun getMostPopularMovies(): Flow<List<Movie>> {
        val movieList = movieLocalDataSource.getPopularMovies().map {
            when {
                it.isEmpty() -> {
                    insertFreshPopularMoviesToCache()
                    it
                }
                dataRefreshManagerImpl.checkIfRefreshNeeded() -> {
                    insertFreshPopularMoviesToCache()
                    it
                }
                else -> {
                    it
                }
            }
        }
        return movieList
    }

    /**
     * Only local data source keeps favorite movies
     */
    override suspend fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieLocalDataSource.getFavoriteMovies()
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        var fullMovieDataFlow = movieLocalDataSource.getMovieById(movieId)
            .distinctUntilChanged()
            .map {
                if (it == null) {
                    insertFreshMovieDataToCache(movieId)
                } else {
                    if (it.trailer == null) {
                        insertTrailerToCache(it.movie_id)
                    }
                }
            it

        }
        return fullMovieDataFlow
    }

    override suspend fun toggleFavorite(movieId: Int) {
        movieLocalDataSource.toggleFavorite(movieId)
    }

    private suspend fun insertTrailerToCache(movieId: Int) {
        val trailer: Flow<Trailer?> = movieNetworkDataSource.getTrailer(movieId)
        if (trailer.count() > 0) {
            movieLocalDataSource.insertTrailer(movieId, trailer.first())
        }
    }

    private suspend fun insertFreshMovieDataToCache(movieId:Int) {
        val fullMovieData = movieNetworkDataSource.getMovieById(movieId)
        movieLocalDataSource.insertMovie(fullMovieData)
    }

    private suspend fun insertFreshPopularMoviesToCache() {
        // get fresh movies from network data source
        val popularMovies = movieNetworkDataSource.getFreshPopularMovies()
        // insert into local data source
        movieLocalDataSource.insertFreshPopularMovies(popularMovies)
    }

    override suspend fun reloadMovies() {
        // get fresh movies from network data source
        insertFreshPopularMoviesToCache()
    }

    override suspend fun getMovieCast(movieId: Int): Flow<List<Role?>> {
        // get character list from the source of truth = database
        val movieCast = movieLocalDataSource.getMovieCast(movieId).map {
            when {
                it.isEmpty() -> {
                    insertCastToCache(movieId)
                    it
                }
                else -> {
                    it
                }
            }
        }
        return movieCast
    }

    private suspend fun insertCastToCache(movieId: Int) {
        // get cast list from network data source
        val castList: List<Role?> = movieNetworkDataSource.getFreshMovieCast(movieId)
        // insert into local data source
        movieLocalDataSource.insertCast(castList)
    }

    override suspend fun getMovieProviders(movieId: Int): Flow<List<Provider?>> {
        // get providers for this movie from the source of truth = database
        val movieProviders = movieLocalDataSource.getProviders(movieId).map {
            when {
                it.isEmpty() -> {
                    insertMovieProvidersToCache(movieId)
                    it
                }
                else -> {
                    it
                }
            }
        }
        return movieProviders
    }

    private suspend fun insertMovieProvidersToCache(movieId: Int) {
        // get providers from network data source
        val providerList: List<Provider?> = movieNetworkDataSource.getFreshProviders(movieId)
        // insert into local data source
        movieLocalDataSource.insertProviders(providerList)
    }

}