package com.gemma.popularmovies.data.cache.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gemma.popularmovies.data.cache.MovieDatabase
import com.gemma.popularmovies.data.cache.daos.ProviderDao
import com.gemma.popularmovies.data.cache.model.CachedProvider
import com.gemma.popularmovies.data.cache.model.CachedProviderAggregate
import com.gemma.popularmovies.data.cache.model.CachedProviderForMovie
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
class ProviderDaoAndroidTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDatabase: MovieDatabase
    private lateinit var providerDao: ProviderDao

    // fake data
    val movieId = 497698;

    @Before
    fun initDb() {
        movieDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        )
            .setTransactionExecutor(Executors.newSingleThreadExecutor()) // for DB transactions
            .build()
        providerDao = movieDatabase.providerDao()
    }

    @Test
    fun insertProviders_getMovieProvidersReturnsCorrect(): Unit = runBlocking {
        // insert mock data
        val providers = mutableListOf<CachedProvider>()
        val provider1 = CachedProvider(337,"Disney Plus", "/mIGNMa4QE8E5fIABDH6lWfy5qhn.jpg")
        val provider2 = CachedProvider(10,"Amazon Video", "/sVBEF7q7LqjHAWSnKwDbzmr2EMY.jpg")
        val provider3 = CachedProvider(2,"Apple iTunes", "/q6tl6Ib6X5FT80RMlcDbexIo4St.jpg")
        providers.add(provider1)
        providers.add(provider2)
        providers.add(provider3)
        providerDao.insertProviders(providers)

        // get it back and check it
        val insertedProviders = providerDao.getMovieProviders(movieId)
        insertedProviders.map {
            Assert.assertEquals(it.count(), 3)
            Assert.assertEquals(it[0], provider1)
            Assert.assertEquals(it[0]?.provider?.name, "Disney Plus")
        }

    }

    @Test
    fun insertProvidersForMovie_getMovieProvidersReturnsCorrect(): Unit = runBlocking {
        // insert mock data
        val providers = mutableListOf<CachedProviderForMovie>()
        val provider1 = CachedProviderForMovie(0, movieId, 337, "flatrate")
        val provider2 = CachedProviderForMovie(0, movieId, 10, "buy")
        val provider3 = CachedProviderForMovie(0, movieId, 2,"rent")
        providers.add(provider1)
        providers.add(provider2)
        providers.add(provider3)
        providerDao.insertProvidersForMovie(providers)

        // get it back and check it
        val insertedRoles = providerDao.getMovieProviders(movieId)
        insertedRoles.map {
            Assert.assertEquals(it.count(), 3)
            Assert.assertEquals(it[0], provider1)
            Assert.assertEquals(it[0]?.providerForMovie?.provider_id, 337)
            Assert.assertEquals(it[0]?.providerForMovie?.type, "flatrate")
        }
    }

    @Test
    fun getMovieProvidersOnEmpty_returnCorrect(): Unit = runBlocking {
        var providers = providerDao.getMovieProviders(movieId)
        Assert.assertEquals(providers.first(), emptyList<CachedProviderAggregate>())
        val emptyList = emptyList<CachedRoleAggregate>()
        providers.map {
            Assert.assertEquals(emptyList, it)
        }
    }
}