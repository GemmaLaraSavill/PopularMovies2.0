package com.gemma.popularmovies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.gemma.popularmovies.data.cache.model.CachedMovieMinimal
import com.gemma.popularmovies.data.network.MovieRemoteMediator
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Provider
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeDataSource : MovieDataSource {

    private var fakeData = FakeData()


    override fun getFavoriteMovies(): Flow<List<Movie>> {
        val movieList = fakeData.getMovieList()
        val favList = movieList.filter { it.favorite > 0 }
        return flow {
            emit(favList)
        }
    }

    override suspend fun refreshMovies(popularMovies: List<Movie>) {
        // not necessary for testing
    }

    override suspend fun getFreshPopularMovies(page: Int): List<Movie> {
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
        // simulates getting data from network
        fakeData.getMovieList().add(fakeData.getMovieById())
    }

    override suspend fun toggleFavorite(movieId: Int) {
        val movieFlow = getMovieById(movieId)
        val movie = movieFlow.first()
        if (movie?.favorite == 0) {
            movie.favorite = 1
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

    override suspend fun getFreshMovieCast(movieId: Int): List<Role?> {
        // cast for 566525 = Shang-Chi and the Legend of the Ten Rings
        return listOf(
            Role(
                1489211,
                566525,
                "Simu Liu",
                "https://image.tmdb.org/t/p/w154/zrJjYjOYzDj7eY9oiHAoz8Yh0yk.jpg",
                "Shaun / Shang-Chi"
            ),
            Role(
                90633,
                566525,
                "Tony Leung Chiu-wai",
                "https://image.tmdb.org/t/p/w154/nQbSQAws5BdakPEB5MtiqWVeaMV.jpg",
                "Xu Wenwu / The Mandarin"
            ),
            Role(
                1742596,
                566525,
                "Awkwafina",
                "https://image.tmdb.org/t/p/w154/l5AKkg3H1QhMuXmTTmq1EyjyiRb.jpg",
                "Katy Chen"
            )
        )
    }

    override suspend fun getMovieCast(movieId: Int): Flow<List<Role?>> {
        // get the cast for my movie
        val castList = fakeData.getMovieCast().filter {
            it.movie_id == movieId
        }
        if (castList.count() > 0) {
            // found cast for this movie in local cache (BD)
            return flow {
                emit(castList)
            }
        } else {
            // NOT found cast for this movie in local cache (BD)
            // insert the cast for the movie from the "network"
            val updatedCastList = fakeData.getNetworkMovieCast()
            val castFromNetwork = updatedCastList.filter {
                it.movie_id == movieId
            }
            return flow {
                emit(castFromNetwork)
            }
        }
    }

    override suspend fun insertCast(roleList: List<Role?>) {
        // not necessary for testing
    }

    override suspend fun getProviders(movieId: Int): Flow<List<Provider?>> {
        return flow {
            emit(fakeData.getProviderList())
        }
    }

    override suspend fun getFreshProviders(movieId: Int): List<Provider?> {
        return fakeData.getProviderList()
    }

    override suspend fun insertProviders(providerList: List<Provider?>) {
        // not necessary for testing
    }

    @ExperimentalPagingApi
    override suspend fun getPagedMovies(moviesRemoteMediator: MovieRemoteMediator): Flow<PagingData<CachedMovieMinimal>> {
        // not necessary for testing as paging data comes straight from Room DB
        return emptyFlow()
    }

    override suspend fun addFreshPopularMovies(movies: List<Movie>) {
        // not necessary for testing
    }

    override suspend fun countMovies(): Int {
        return fakeData.getMovieList().count()
    }


}