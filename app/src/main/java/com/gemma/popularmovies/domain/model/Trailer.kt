package com.gemma.popularmovies.domain.model

data class Trailer(
    val movie_id: Int,
    val trailer_id: String,
    val key: String,
    val site: String,
    val name: String?
)