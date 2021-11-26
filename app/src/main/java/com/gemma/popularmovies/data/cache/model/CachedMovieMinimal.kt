package com.gemma.popularmovies.data.cache.model

import com.gemma.popularmovies.domain.model.Movie

data class CachedMovieMinimal (
    val movie_id: Int,
    val title: String?,
    var poster: String?,
    val favorite: Int
) {
    fun toDomain() = Movie(movie_id, title, poster, null, null, null, null, favorite, null)
}