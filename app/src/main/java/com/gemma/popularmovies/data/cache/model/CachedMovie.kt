package com.gemma.popularmovies.data.cache.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Trailer

@Entity(tableName = "movies",
    indices = [Index(value = ["movie_id"], unique = true)])
data class CachedMovie(
    @PrimaryKey(autoGenerate = true) val movie_id: Int,
    val title: String?,
    var poster: String?,
    var backdrop: String?,
    val overview: String?,
    val rating: String?,
    val release_date: String?,
    val favorite: Int = 0,
    val page: Int = 0
) {
    fun toDomain(trailer: Trailer?) = Movie(movie_id, title, poster, backdrop, overview, rating, release_date, favorite, trailer, page)
}