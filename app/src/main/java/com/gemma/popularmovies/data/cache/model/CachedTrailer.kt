package com.gemma.popularmovies.data.cache.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gemma.popularmovies.domain.model.Trailer

@Entity(
    tableName = "trailers", foreignKeys = [
        androidx.room.ForeignKey(
            entity = CachedMovie::class,
            parentColumns = ["movie_id"],
            childColumns = ["movie_id"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )],
    indices = [Index("movie_id")]
)
data class CachedTrailer(
    @PrimaryKey val movie_id: Int,
    val trailer_id: String,
    val key: String,
    val site: String,
    val name: String?
) {
    fun toDomain() = Trailer(movie_id, trailer_id, key, site, name)
}