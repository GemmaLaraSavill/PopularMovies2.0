package com.gemma.popularmovies.data.cache.model

import androidx.room.Embedded
import androidx.room.Relation
import com.gemma.popularmovies.domain.model.Provider

data class CachedProviderAggregate(
    @Embedded
    val providerForMovie: CachedProviderForMovie,
    @Relation(parentColumn = "provider_id", entityColumn = "provider_id")
    val provider: CachedProvider
) {
    fun toDomain() =
        Provider(
            provider_id = provider.provider_id,
            movie_id = providerForMovie.movie_id,
            name = provider.name,
            logo = provider.logo,
            type = providerForMovie.type
        )
}