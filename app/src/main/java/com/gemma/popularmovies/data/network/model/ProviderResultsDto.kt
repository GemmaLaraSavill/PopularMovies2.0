package com.gemma.popularmovies.data.network.model

import com.google.gson.annotations.SerializedName

class ProviderResultsDto(
    @SerializedName("id") val movie_id: Int,
    @SerializedName("results") val countries: ProviderCountryDto
)
