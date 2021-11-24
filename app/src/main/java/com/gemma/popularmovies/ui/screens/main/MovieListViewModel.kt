package com.gemma.popularmovies.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var movieList: Flow<List<Movie>>
    var favoriteMovieList: Flow<List<Movie>>

    init {
        movieList = loadMovies()
        favoriteMovieList = loadFavoriteMovies()
    }

    /**
     * Gets a list of popular movies
     */
    private fun loadMovies(): Flow<List<Movie>> {
        var movieListFromRepo: Flow<List<Movie>> = emptyFlow()
        viewModelScope.launch {
            movieListFromRepo = getMovieListUseCase.getMostPopularMovies()
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
}

