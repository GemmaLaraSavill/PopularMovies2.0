package com.gemma.popularmovies.data.network.model

import com.gemma.popularmovies.domain.model.Provider
import com.google.gson.annotations.SerializedName

data class ProviderDto(
    @SerializedName("display_priority") val display_priority: String,
    @SerializedName("logo_path") val logo: String,
    @SerializedName("provider_id") val provider_id: Int,
    @SerializedName("provider_name") val name: String,
) {
    fun toDomain(movie_id:Int, type:String) = Provider(provider_id, movie_id, name, logo, type)
}
