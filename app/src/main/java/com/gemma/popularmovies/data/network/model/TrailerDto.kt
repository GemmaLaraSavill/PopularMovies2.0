package com.gemma.popularmovies.data.network.model

import com.gemma.popularmovies.domain.model.Trailer
import com.google.gson.annotations.SerializedName

data class TrailerDto(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("site") val site: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("official") val official: Boolean,
    @SerializedName("published_at") val date: Boolean

) {
    fun toDomain() = Trailer(0, id, key, site, name)
}