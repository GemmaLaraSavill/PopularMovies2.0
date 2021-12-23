package com.gemma.popularmovies.domain.model

data class Provider(
    val provider_id: Int,
    val movie_id: Int,
    val name: String?,
    val logo: String?,
    val type: String?
)