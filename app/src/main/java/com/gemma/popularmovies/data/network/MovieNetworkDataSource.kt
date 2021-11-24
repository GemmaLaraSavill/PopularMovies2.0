package com.gemma.popularmovies.data.network

import android.content.Context
import android.util.Log
import com.gemma.popularmovies.data.MovieDataSource
import com.gemma.popularmovies.data.network.model.TrailerDto
import com.gemma.popularmovies.data.network.model.TrailersPageDto
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Manages data in and out the API
 */
class MovieNetworkDataSource @Inject constructor(private val moviesApiService: MoviesApiService, private val context: Context) : MovieDataSource {

    override suspend fun getPopularMovies(): Flow<List<Movie>> {
        // Not required for the remote data source
        return emptyFlow()
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        // Not required for the remote data source
        return emptyFlow()
    }

    override suspend fun insertFreshPopularMovies(popularMovies: List<Movie>) {
        // Not required for the remote data source
    }

    /**
     * Load 1 page of most popular movies from the API
     */
    override suspend fun getFreshPopularMovies(): List<Movie> {
        return if (NetworkConnectivityManager.isNetworkConnected(context)) {
            var popularMovies: List<Movie> = moviesApiService.getPopularMovies(1).movieList.map {
                it.toDomain()
            }
            popularMovies
        } else {
            emptyList()
        }
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        return if (NetworkConnectivityManager.isNetworkConnected(context)) {
            var movieData = moviesApiService.getFullMovieData(movieId)
            flow { emit(movieData.toDomain()) }
        } else {
            emptyFlow()
        }
    }


    override suspend fun insertMovie(fullMovieData: Flow<Movie?>) {
        // Not required for the remote data source
    }

    override suspend fun toggleFavorite(movieId: Int) {
        // Not required for the remote data source
    }

    /**
     * Filters the movie videos to the the latest official trailer
     * Type must be a Trailer
     * The site must be YouTube
     * And it must be official
     */
    override suspend fun getTrailer(movieId: Int): Flow<Trailer?> {
        if (NetworkConnectivityManager.isNetworkConnected(context)) {
            val videosPage: TrailersPageDto = moviesApiService.getVideos(movieId)
            val videos = videosPage.videoList
            val trailer: List<TrailerDto> = videos.filter {
                it.type == "Trailer"
                it.site == "YouTube"
                it.official
            }.sortedBy { it.date }
            return if (trailer.count() > 0) {
                flow {
                    emit(trailer.first().toDomain())
                }
            } else {
                emptyFlow()
            }
        } else {
            return emptyFlow()
        }
    }

    override suspend fun insertTrailer(movieId:Int, trailer: Trailer?) {
        // Not required for the remote data source
    }


}