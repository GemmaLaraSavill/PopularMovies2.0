package com.gemma.popularmovies.data.cache.model

import androidx.room.Embedded
import androidx.room.Relation
import com.gemma.popularmovies.domain.model.Role

data class CachedRoleAggregate(
    @Embedded
    val role: CachedRole,
    @Relation(parentColumn = "artist_id", entityColumn = "artist_id")
    val artist: CachedArtist
) {
    fun toDomain() =
        Role(role.artist_id, role.movie_id, artist.name, artist.image, role.character)
}