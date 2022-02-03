package com.gemma.popularmovies.data.cache.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gemma.popularmovies.data.cache.MovieDatabase
import com.gemma.popularmovies.data.cache.daos.MovieDao
import com.gemma.popularmovies.data.cache.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.isNull
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class MovieDaoAndroidTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDatabase: MovieDatabase
    private lateinit var movieDao: MovieDao

    // fake data
    private val moviePopular1 = CachedMovie(1, "Title 1", "Poster 1", "Backdrop 1", "Overview 1", "1", "2021-10-29", 0)
    private val moviePopular2 = CachedMovie(2, "Title 2", "Poster 2", "Backdrop 2","Overview 2", "2", "2021-10-29", 0)

    @Before
    fun initDb() {
        movieDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        )
            .setTransactionExecutor(Executors.newSingleThreadExecutor()) // for DB transactions
            .build()
        movieDao = movieDatabase.movieDao()
    }


    @Test
    fun countMovies_returnsCorrect(): Unit = runBlocking {
        val insertedPopularMovies = insertFakeMostPopularMovies()
        val movieCount = movieDao.countMovies()
        assertEquals(insertedPopularMovies.count(), movieCount)
    }

    /**
     * Popular Movies transaction: delete + 2 inserts
     */
    @Test
    fun insertFreshPopularMovies_returnsCorrectDBCount(): Unit = runBlocking {
        // insert mock data
        var insertedMovies = insertFakeMostPopularMovies()
        assertEquals(2, insertedMovies.count())

        // insert again to make sure they are being deleted before inserted
        insertedMovies = insertFakeMostPopularMovies()
        assertEquals(2, insertedMovies.count())

        // delete Most Popular movies returns both tables to zero
        var movieCount = movieDao.countMovies()
        assertEquals(2, movieCount)
        movieDao.deleteAllNonFavoriteMovies()
        movieCount = movieDao.countMovies()
        assertEquals(0, movieCount)


    }

    @Test
    fun makeMoviesFavorite_setsFavoriteMovies() {
        runBlocking {
            insertFakeMostPopularMovies()
            movieDao.toggleFavorite(1)
            runBlocking() {
                movieDao.getFavoriteMovies().map {
                    it.map {
                        assertEquals(1, it.movie_id)
                    }
                }
            }
        }
    }

    @Test
    fun makeMoviesNonFavorite_unsetsFavoriteMovies()  {
        runBlocking {
            insertFakeMostPopularMovies()
            movieDao.toggleFavorite(1)
            runBlocking() {
                movieDao.getFavoriteMovies().map {
                    assertEquals(isNull(), it)
                }
            }
        }
    }

    @Test
    fun deleteAllNonFavoritePopularMovies_favoritesRemain()  {
        runBlocking {
            insertFakeMostPopularMovies()
            movieDao.toggleFavorite(1)
            runBlocking {
                movieDao.deleteAllNonFavoriteMovies()
            }
            var movies: Flow<List<CachedMovieMinimal>> = movieDao.getMostPopularMovies()
            movies.map {
                it.map {
                    assertEquals(1, it.movie_id)
                }
            }
        }
    }


    /**
     * Helper function that inserts two Popular Movies
     */
    private fun insertFakeMostPopularMovies():List<Long> = runBlocking {

        // insert mock data
        val movieList = mutableListOf<CachedMovie>()
        movieList.add(moviePopular1)
        movieList.add(moviePopular2)

        // insert to movies
        return@runBlocking movieDao.refreshMovies(movieList)

    }


    @Test
    fun getMostPopularMovies_returnsCorrectMovies() {
        // insert data
        var movieIds = insertFakeMostPopularMovies()
        // expected data
        val popularMovieList = mutableListOf<CachedMovie>()
        popularMovieList.add(moviePopular1)
        popularMovieList.add(moviePopular2)
        // verify
        movieDao.getMostPopularMovies().map {
            assertEquals(popularMovieList, it)
        }
    }

    @Test
    fun getFavoriteMovies_returnsCorrectMovies()  {
        runBlocking {
            // insert data
            var movieMPids = insertFakeMostPopularMovies()

            movieDao.toggleFavorite(1)

            // expected data
            val favoriteMovieList = mutableListOf<CachedMovie>()
            favoriteMovieList.add(moviePopular1)
            // verify
            movieDao.getFavoriteMovies().map {
                assertEquals(favoriteMovieList, it)
            }
        }
    }

    @Test
    fun getFullMovieData_returnsCorrectMovieData() {
        runBlocking {
            insertFakeMostPopularMovies()
            runBlocking {
                val movieFlow: Flow<CachedMovieAggregate?> = movieDao.getFullMovieData(1)
                assertEquals(moviePopular1, movieFlow.first()?.movie)
            }
            // now set as favorite
            movieDao.toggleFavorite(1)
            val moviePopular1Fav = moviePopular1.copy(favorite = 1)
            runBlocking {
                val movieFlow: Flow<CachedMovieAggregate?> = movieDao.getFullMovieData(1)
                assertEquals(moviePopular1Fav, movieFlow.first()?.movie)
            }
        }
    }

    @Test
    fun getFullMovieData_returnsMovieNotFound() {
        runBlocking {
            val movieFlow: Flow<CachedMovieAggregate?> = movieDao.getFullMovieData(1)
            assertEquals(null, movieFlow.first()?.movie)
        }
    }

    @Test
    fun insertMovie() = runBlocking {
        val newMovie = moviePopular1.copy(movie_id = 3)
        movieDao.insertMovie(newMovie)
        val extractedMovieFlow = movieDao.getFullMovieData(3)
        assertEquals(newMovie, extractedMovieFlow.first()?.movie)

    }
    

    @After
    fun closeDb() {
        movieDatabase.close()
    }
}