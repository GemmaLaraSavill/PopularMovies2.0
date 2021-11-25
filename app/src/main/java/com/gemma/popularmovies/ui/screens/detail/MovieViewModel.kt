package com.gemma.popularmovies.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.usecase.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val getMovieUseCase: GetMovieDetailUseCase) :
    ViewModel() {

    var movie: Flow<Movie?> = emptyFlow()
    var cast: Flow<List<Role?>>

    init {
        movie = getMovieById(null)
        cast = getCast(null)
    }

    /**
     * Gets the full data of a movie
     * Includes a trailer
     */
    fun getMovieById(movieId: Int?): Flow<Movie?> {
        if (movieId != null) {
            viewModelScope.launch {
                movie = getMovieUseCase.getMovieDetail(movieId)
                    .distinctUntilChanged()
                    .map {
                        it
                    }
            }
        }
        return movie
    }

    fun onFavoriteActionClicked(movieId: Int?) {
        if (movieId != null) {
            viewModelScope.launch {
                getMovieUseCase.toggleFavorite(movieId)
            }
        }
        movie.map {
            if (it?.favorite == 0) {
                it.favorite = 1
            } else {
                it?.favorite = 0
            }
        }
    }

    fun getCast(movieId: Int?): Flow<List<Role?>> {
        if (movieId != null) {
            viewModelScope.launch {
                cast = getMovieUseCase.getMovieCast(movieId)
            }
        }
        return cast
    }
}

