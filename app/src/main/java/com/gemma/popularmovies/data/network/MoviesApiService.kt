package com.gemma.popularmovies.data.network

import com.gemma.popularmovies.data.network.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    @GET(ApiConstants.GET_POPULAR)
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("include_adult") adult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): MoviesPageDto

    @GET(ApiConstants.MOVIE)
    suspend fun getFullMovieData(@Path("movie_id") movieId: Int):MovieDto

    @GET(ApiConstants.VIDEOS)
    suspend fun getVideos(@Path("id") movieId: Int): TrailersPageDto

    @GET(ApiConstants.CREDITS)
    suspend fun getCredits(@Path("id") movieId: Int): CastDto

    @GET(ApiConstants.PROVIDERS)
    suspend fun getProviders(@Path("id") movieId: Int): ProviderResultsDto

}