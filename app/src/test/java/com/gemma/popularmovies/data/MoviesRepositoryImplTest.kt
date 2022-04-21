package com.gemma.popularmovies.data

import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Provider
import com.gemma.popularmovies.domain.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MoviesRepositoryImplTest {

    private lateinit var localDataSource: MovieDataSource
    private lateinit var remoteDataSource: MovieDataSource
    private lateinit var dataRefreshManager: FakeDataRefreshManager

    private lateinit var fakeData: FakeData

    // Class under test
    private lateinit var moviesRepository: MoviesRepositoryImpl

    @Before
    fun createRepository() {
        localDataSource = FakeDataSource()
        remoteDataSource = FakeDataSource()
        dataRefreshManager = FakeDataRefreshManager()

        fakeData = FakeData()

        // Get a reference to the class under test
        moviesRepository = MoviesRepositoryImpl(
            localDataSource, remoteDataSource, dataRefreshManager
        )
    }

    @Test
    fun getFavoriteMovies_returnsFavoriteMoviesFlow() = runBlocking {
        // Before: get the movie list from the repository
        val favoriteMovieList = moviesRepository.getFavoriteMovies()
        val movieList: List<Movie> = favoriteMovieList.first() // Returns the first item in the flow
        // After: assert that the first movie is a favorite
        assertEquals(movieList[0].favorite, 1)
        // only one of the fake data is a favorite
        assertEquals(1, movieList.count())
    }



    @Test
    fun getFullMovieData_returnsMovie() = runBlocking {
        // Before: get the movie from the repository
        val movieId = 524434 // id of Eternals
        val movieFlow: Flow<Movie?> = moviesRepository.getMovieById(movieId)
        // check that the correct movie is received
        assertEquals("Eternals", movieFlow.first()?.title)
    }

    @Test
    fun getFullMovieDataOnEmptyMovie_returnsMovieFromNetwork() = runBlocking {
        // Before: get a movie that doesn't exist in repository (local cache)
        val movieId = 566525 // id of Shang-Chi and the Legend of the Ten Rings
        val movieFlow: Flow<Movie?> = moviesRepository.getMovieById(movieId)
        // check that the movie has been added and received
        val expectedMovie = fakeData.getMovieById()
        assertEquals(expectedMovie, movieFlow.first())
    }

    @Test
    fun toggleFavoriteMovies_changesDB() {
        runBlocking {
            val movieId = 566525 // id of Shang-Chi and the Legend of the Ten Rings
            localDataSource.toggleFavorite(movieId)
            val movieFlow: Flow<Movie?> = moviesRepository.getMovieById(movieId)
            assertEquals(1, movieFlow.first()?.favorite)
        }
    }

    @Test
    fun getMovieCast_returnsCast() = runBlocking {
        // Before: get the movie from the repository
        val movieId = 512195
        val movieCastFlow: Flow<List<Role?>> = moviesRepository.getMovieCast(movieId)
        // check that the correct roles are received
        assertEquals("Ryan Reynolds", movieCastFlow.first().first()?.name)
        assertEquals("Nolan Booth", movieCastFlow.first().first()?.character)
    }

    @Test
    fun getMovieCastOnEmptyMovie_returnsCastFromNetwork() = runBlocking {
        // Before: get cast for a movie that doesn't have it's cast local cache
        val movieId = 566525 // id of Shang-Chi and the Legend of the Ten Rings
        val movieCastFlow: Flow<List<Role?>> = moviesRepository.getMovieCast(movieId)
        // check that the repository has collected the movie from the network
        // has been added to the local data cache and returned
        assertEquals(3, movieCastFlow.first().count())
        assertEquals("Simu Liu", movieCastFlow.first().first()?.name)
        assertEquals("Shaun / Shang-Chi", movieCastFlow.first().first()?.character)
    }

    @Test
    fun getMovieProvidersOnEmptyMovie_returnsProvidersFromNetwork() = runBlocking {
        // Before: get providers for a movie that doesn't have it's cast local cache
        val movieId = 566525 // id of Shang-Chi and the Legend of the Ten Rings
        val movieProvidersFlow: Flow<List<Provider?>> = moviesRepository.getMovieProviders(movieId)
        // check that the movie has been added and received
        val expectedProviders = fakeData.getProviderList().first()
        assertEquals(expectedProviders, movieProvidersFlow.first().first())
    }

    @Test
    fun getMovieProviders_returnsProviders() = runBlocking {
        // Before: get the movie from the repository
        val movieId = 497698 // id of BlackWidow
        val movieProvidersFlow: Flow<List<Provider?>> = moviesRepository.getMovieProviders(movieId)
        // check that the correct provider is received
        assertEquals("Disney Plus", movieProvidersFlow.first().first()?.name)
    }


}