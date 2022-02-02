package com.gemma.popularmovies.data.cache.model

import androidx.room.Embedded
import androidx.room.Relation
import com.gemma.popularmovies.domain.model.Movie

data class CachedMovieAggregate(
    @Embedded
    val movie: CachedMovie,
    @Relation(parentColumn = "movie_id", entityColumn = "movie_id")
    val trailer: CachedTrailer?,
) {
    fun toDomain() =
        Movie(movie.movie_id, movie.title, movie.poster, movie.backdrop, movie.overview, movie.rating, movie.release_date, movie.favorite, trailer?.toDomain(), movie.page)
}