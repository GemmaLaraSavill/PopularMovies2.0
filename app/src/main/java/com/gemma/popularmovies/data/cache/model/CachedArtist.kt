package com.gemma.popularmovies.data.cache.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Table for the artists, one artist can have roles in different movies
@Entity(tableName = "artist",
    indices = [Index(value = ["id"], unique = true)])
data class CachedArtist(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val artist_id: Int,
    val name: String?,
    var image: String?
)