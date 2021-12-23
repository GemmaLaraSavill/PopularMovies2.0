package com.gemma.popularmovies.data.cache.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "movie_provider",
    indices = [Index(value = ["id"], unique = true)])
data class CachedProviderForMovie(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val movie_id: Int,
    var provider_id: Int,
    var type: String?
)