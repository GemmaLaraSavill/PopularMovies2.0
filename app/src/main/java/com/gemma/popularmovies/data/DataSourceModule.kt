package com.gemma.popularmovies.data

import com.gemma.popularmovies.data.cache.MovieLocalDataSource
import com.gemma.popularmovies.data.network.MovieNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class LocalDataSourceModule

@Qualifier
annotation class NetworkDataSourceModule

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Singleton
    @Binds
    @LocalDataSourceModule
    abstract fun bindMovieLocalDataSource(impl: MovieLocalDataSource): MovieDataSource

    @Singleton
    @Binds
    @NetworkDataSourceModule
    abstract fun bindMovieNetworkDataSource(impl: MovieNetworkDataSource): MovieDataSource
}