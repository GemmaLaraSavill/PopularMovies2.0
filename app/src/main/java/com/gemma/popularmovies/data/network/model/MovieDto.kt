package com.gemma.popularmovies.data.network.model

import com.gemma.popularmovies.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val movie_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val poster: String,
    @SerializedName("backdrop_path") val backdrop: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val rating: String,
    @SerializedName("release_date") val release_date: String
) {
    fun toDomain(page:Int) = Movie(movie_id, title, poster,backdrop, overview, rating, release_date, 0, null, page)
}