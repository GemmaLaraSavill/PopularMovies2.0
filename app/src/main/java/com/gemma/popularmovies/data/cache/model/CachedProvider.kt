package com.gemma.popularmovies.data.cache.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "provider",
    indices = [Index(value = ["provider_id"], unique = true)])
data class CachedProvider(
    @PrimaryKey(autoGenerate = true) val provider_id: Int,
    val name: String?,
    var logo: String?
)