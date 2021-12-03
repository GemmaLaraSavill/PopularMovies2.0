package com.gemma.popularmovies.data.cache.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gemma.popularmovies.data.cache.MovieDatabase
import com.gemma.popularmovies.data.cache.daos.CastDao
import com.gemma.popularmovies.data.cache.model.CachedArtist
import com.gemma.popularmovies.data.cache.model.CachedRole
import com.gemma.popularmovies.data.cache.model.CachedRoleAggregate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class CastDaoAndroidTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDatabase: MovieDatabase
    private lateinit var castDao: CastDao

    // fake data
    val movieId = 512195;

    @Before
    fun initDb() {
        movieDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        )
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        castDao = movieDatabase.castDao()
    }

    @Test
    fun insertArtists_getMovieCastReturnsCorrect(): Unit = runBlocking {
        // insert mock data
        val artists = mutableListOf<CachedArtist>()
        val artist1 = CachedArtist(0, 10859,"Ryan Reynolds", "4SYTH5FdB0dAORV98Nwg3llgVnY.jpg")
        val artist2 = CachedArtist(0, 90633,"Gal Gadot", "fysvehTvU6bE3JgxaOTRfvQJzJ4.jpg")
        artists.add(artist1)
        artists.add(artist2)
        castDao.insertArtists(artists)

        // get it back and check it
        val insertedArtists = castDao.getMovieCast(movieId)
        insertedArtists.map {
            Assert.assertEquals(it.count(), 2)
            Assert.assertEquals(it[0], artist1)
            Assert.assertEquals(it[0]?.artist?.name, "Ryan Reynolds")
        }

    }

    @Test
    fun insertRoles_getMovieCastReturnsCorrect(): Unit = runBlocking {
        // insert mock data
        val roles = mutableListOf<CachedRole>()
        val role1 = CachedRole(0, 10859,movieId,"Nolan Booth")
        val role2 = CachedRole(0, 90633,movieId, "Sarah Black / The Bishop")
        roles.add(role1)
        roles.add(role2)
        castDao.insertRoles(roles)

        // get it back and check it
        val insertedRoles = castDao.getMovieCast(movieId)
        insertedRoles.map {
            Assert.assertEquals(it.count(), 2)
            Assert.assertEquals(it[0], role1)
            Assert.assertEquals(it[0]?.role?.character, "Nolan Booth")
        }
    }

    @Test
    fun getMovieCastOnEmpty_returnCorrect(): Unit = runBlocking {
        var cast = castDao.getMovieCast(movieId)
        Assert.assertEquals(cast.first(), emptyList<CachedRoleAggregate>())
        val emptyList = emptyList<CachedRoleAggregate>()
        cast.map {
            Assert.assertEquals(emptyList, it)
        }
    }
}