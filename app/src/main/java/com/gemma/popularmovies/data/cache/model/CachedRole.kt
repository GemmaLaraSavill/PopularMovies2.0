package com.gemma.popularmovies.data.cache.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Table for the roles, connects artists to movies
@Entity(tableName = "role",
    indices = [Index(value = ["id"], unique = true)])
data class CachedRole(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val artist_id: Int,
    val movie_id: Int,
    var character: String?
)