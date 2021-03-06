package com.gemma.popularmovies.data.cache

import android.content.Context
import androidx.room.Room
import com.gemma.popularmovies.data.cache.daos.CastDao
import com.gemma.popularmovies.data.cache.daos.MovieDao
import com.gemma.popularmovies.data.cache.daos.ProviderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): MovieDatabase {
        return Room.databaseBuilder(appContext, MovieDatabase::class.java, "movies").build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao {
        return movieDatabase.movieDao()
    }


    @Singleton
    @Provides
    fun provideCastDao(movieDatabase: MovieDatabase): CastDao {
        return movieDatabase.castDao()
    }

    @Singleton
    @Provides
    fun provideMovieProviderDao(movieDatabase: MovieDatabase): ProviderDao {
        return movieDatabase.providerDao()
    }
}