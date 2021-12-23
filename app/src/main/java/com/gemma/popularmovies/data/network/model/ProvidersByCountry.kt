package com.gemma.popularmovies.data.network.model

import com.google.gson.annotations.SerializedName

class ProvidersByCountry (
    @SerializedName("link") val link: String,
    @SerializedName("flatrate") val flatRateProviders: List<ProviderDto?>,
    @SerializedName("rent") val rentProviders: List<ProviderDto?>,
    @SerializedName("buy") val buyProviders: List<ProviderDto?>
)
