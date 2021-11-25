package com.gemma.popularmovies.data.network.model

import com.gemma.popularmovies.domain.model.Role
import com.google.gson.annotations.SerializedName

class RoleDto(
    @SerializedName("id") val artist_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("profile_path") val image: String,
    @SerializedName("character") val character: String
){
    fun toDomain(movieId:Int) = Role(artist_id, movieId, name, image, character)
}
