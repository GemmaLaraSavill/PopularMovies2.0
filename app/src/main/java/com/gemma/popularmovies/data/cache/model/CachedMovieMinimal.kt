package com.gemma.popularmovies.data.cache.model

import com.gemma.popularmovies.domain.model.Movie

data class CachedMovieMinimal (
    val movie_id: Int,
    var poster: String?,
    val favorite: Int
) {
    fun toDomain() = Movie(movie_id, null, poster, null, null, null, null, favorite, null)
}