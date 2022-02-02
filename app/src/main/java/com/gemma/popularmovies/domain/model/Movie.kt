package com.gemma.popularmovies.domain.model

import com.gemma.popularmovies.data.cache.model.CachedMovie

data class Movie(
    val movie_id: Int,
    val title: String?,
    val poster: String?,
    val backdrop: String?,
    val overview: String?,
    val rating: String?,
    val release_date: String?,
    var favorite: Int = 0,
    var trailer: Trailer?,
    val page: Int
) {
    fun toCache() = CachedMovie(movie_id, title, poster, backdrop, overview, rating, release_date, favorite, page)
}