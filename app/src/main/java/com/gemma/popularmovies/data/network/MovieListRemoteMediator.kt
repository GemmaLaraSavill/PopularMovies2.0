package com.gemma.popularmovies.data.network

import androidx.paging.*
import com.gemma.popularmovies.data.MovieDataSource
import com.gemma.popularmovies.data.cache.model.CachedMovieMinimal
import com.gemma.popularmovies.data.network.ApiConstants.ENDING_PAGE_INDEX
import com.gemma.popularmovies.data.network.ApiConstants.NUM_PAGES_IN_CACHE
import com.gemma.popularmovies.data.network.ApiConstants.NUM_RESULTS_PER_PAGE
import com.gemma.popularmovies.data.network.ApiConstants.STARTING_PAGE_INDEX
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.repository.DataRefreshManager
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class MovieRemoteMediator @Inject constructor(
    private val movieLocalDataSource: MovieDataSource,
    private val movieNetworkDataSource: MovieDataSource,
    private val dataRefreshManagerImpl: DataRefreshManager
) : RemoteMediator<Int, CachedMovieMinimal>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedMovieMinimal>
    ): MediatorResult {

        var pageRequested: Int? = getPageForLastItem(state)
        var loadSize = STARTING_PAGE_INDEX * NUM_RESULTS_PER_PAGE

        if (loadType == LoadType.APPEND) {
            pageRequested = pageRequested?.plus(1)
                ?: return MediatorResult.Success(endOfPaginationReached = pageRequested != null)
        } else if (loadType == LoadType.REFRESH && pageRequested == null) {
            val cachedMovieCount = movieLocalDataSource.countMovies()
            loadSize = NUM_PAGES_IN_CACHE * NUM_RESULTS_PER_PAGE
            pageRequested = if (cachedMovieCount < loadSize) {
                1
            } else {
                // don't want to trigger API call
                null
            }
        } else {
            // don't want to trigger API call
            pageRequested = null
        }

        try {
            var endOfPaginationReached = false
            if (pageRequested != null) {
                // get more movies from api service
                val movieList: MutableList<Movie> = mutableListOf()
                while (loadSize > 0) {
                    val moviePage = movieNetworkDataSource.getFreshPopularMovies(pageRequested)
                    movieList.addAll(moviePage)
                    pageRequested += 1
                    loadSize -= NUM_RESULTS_PER_PAGE
                }
                if (dataRefreshManagerImpl.checkIfRefreshNeeded()) {
                    // delete and add
                    movieLocalDataSource.refreshMovies(movieList)
                } else {
                    // only add
                    movieLocalDataSource.addFreshPopularMovies(movieList)
                }
                endOfPaginationReached = movieList.isEmpty()
            }
            if (pageRequested == ENDING_PAGE_INDEX) {
                endOfPaginationReached = true
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    suspend fun reload() {
        val emptyPagingState = PagingState<Int, CachedMovieMinimal>(
            emptyList(), null, PagingConfig(
                NUM_RESULTS_PER_PAGE
            ), 0
        )
        load(LoadType.REFRESH, emptyPagingState)
    }

    /**
     * Get the page of the last Movie item loaded from the database
     * Returns null if no data passed to Mediator
     */
    private fun getPageForLastItem(state: PagingState<Int, CachedMovieMinimal>): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.page
    }



}