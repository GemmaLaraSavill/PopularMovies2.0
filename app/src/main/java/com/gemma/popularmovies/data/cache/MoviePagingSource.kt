package com.gemma.popularmovies.data.cache

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gemma.popularmovies.data.cache.daos.MovieDao
import com.gemma.popularmovies.domain.model.Movie
import javax.inject.Inject


class MoviePagingSource @Inject constructor(private val movieDao: MovieDao) :
    PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return LoadResult.Page(
            data = emptyList(),
            prevKey = 0,
            nextKey = 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}