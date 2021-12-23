package com.gemma.popularmovies.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gemma.popularmovies.data.cache.daos.CastDao
import com.gemma.popularmovies.data.cache.daos.MovieDao
import com.gemma.popularmovies.data.cache.daos.ProviderDao
import com.gemma.popularmovies.data.cache.model.*

@Database(
    entities = [
        CachedMovie::class,
        CachedTrailer::class,
        CachedArtist::class,
        CachedRole::class,
        CachedProvider::class,
        CachedProviderForMovie::class]
    ,
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun castDao(): CastDao
    abstract fun providerDao(): ProviderDao
}