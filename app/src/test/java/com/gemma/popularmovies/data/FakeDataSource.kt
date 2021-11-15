package com.gemma.popularmovies.data

import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeDataSource : MovieDataSource {

    private var fakeData = FakeData()

    override suspend fun getPopularMovies(): Flow<List<Movie>> {
        val movieList = fakeData.getMovieList()
        return flow {
            emit(movieList)
        }
    }


    override fun getFavoriteMovies(): Flow<List<Movie>> {
        val movieList = fakeData.getMovieList()
        val favList = movieList.filter { it.favorite!! > 0 }
        return flow {
            emit(favList)
        }
    }

    override suspend fun insertFreshPopularMovies(popularMovies: List<Movie>) {
        TODO("Not yet implemented")
    }

    override suspend fun getFreshPopularMovies(): List<Movie> {
        return fakeData.getMovieList()
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        val movieList: List<Movie> = fakeData.getMovieList().filter {
            it.movie_id == movieId
        }
        if(movieList.count() > 0) {
            // found movie in local cache (BD)
            return flow {
                emit(movieList[0])
            }
        } else {
            // NOT found movie in local cache (BD)
            // insert the movie from the "network"
            val updatedMovieList = fakeData.getMovieList().toMutableList()
            // add the missing movie
            updatedMovieList.add(fakeData.getMovieById())
            val extractedMovie = updatedMovieList.filter {
                it.movie_id == movieId
            }
            return flow {
                emit(extractedMovie[0])
            }
        }
    }

    override suspend fun insertMovie(fullMovieData: Flow<Movie?>) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(movieId: Int) {
        var movieFlow = getMovieById(movieId)
        var movie = movieFlow.first()
        if (movie?.favorite == 0) {
            movie?.favorite = 1
        } else {
            movie?.favorite = 0
        }
    }

    override suspend fun getTrailer(movieId: Int): Flow<Trailer?> {
        val movieTrailer = Trailer(566525,"606897779ca759005781c348", "Fp9pNPdNwjI", "YouTube", "New Trailer" )
        return flow {
            emit(movieTrailer)
        }
    }

    override suspend fun insertTrailer(movieId: Int, trailer: Trailer?) {
        // not necessary for testing
    }

}