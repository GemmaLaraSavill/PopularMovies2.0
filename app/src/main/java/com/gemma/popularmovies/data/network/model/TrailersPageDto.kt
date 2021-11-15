package com.gemma.popularmovies.data.network.model

import com.google.gson.annotations.SerializedName

data class TrailersPageDto(
    @SerializedName("id") val movie_id: Int,
    @SerializedName("results") val videoList: List<TrailerDto>
)