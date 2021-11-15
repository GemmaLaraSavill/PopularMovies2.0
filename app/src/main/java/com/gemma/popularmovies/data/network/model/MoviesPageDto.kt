package com.gemma.popularmovies.data.network.model

import com.google.gson.annotations.SerializedName

data class MoviesPageDto(
    @SerializedName("page") val pageNumber: Int,
    @SerializedName("results") val movieList: List<MovieDto>
)