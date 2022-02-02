package com.gemma.popularmovies.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.usecase.GetMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val getMovieListUseCase: GetMovieListUseCase) :
    ViewModel() {

    var movieList: Flow<PagingData<Movie>>
    var favoriteMovieList: Flow<List<Movie>>

    init {
        movieList = loadMovies()
        favoriteMovieList = loadFavoriteMovies()
    }

    /**
     * Gets a list of popular movies
     */
    private fun loadMovies(): Flow<PagingData<Movie>> {
        var movieListFromRepo: Flow<PagingData<Movie>> = emptyFlow()
        viewModelScope.launch {
            movieListFromRepo = getMovieListUseCase.getMostPopularMovies()
                .cachedIn(viewModelScope)
        }
        return movieListFromRepo
    }

    /**
     * Gets favorite movie list
     */
    private fun loadFavoriteMovies(): Flow<List<Movie>> {
        viewModelScope.launch {
            favoriteMovieList = getMovieListUseCase.getFavoriteMovies()
        }
        return favoriteMovieList
    }

    fun reloadMovies() {
        viewModelScope.launch {
            getMovieListUseCase.reloadMovies()
        }
    }

    private var movieListScrollState = 0
    fun saveMovieListScrollState(scrollIndex: Int) {
        movieListScrollState = scrollIndex
    }
    fun getMovieListScrollState():Int {
        return movieListScrollState
    }
}

