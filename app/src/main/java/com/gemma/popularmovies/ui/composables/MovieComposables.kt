package com.gemma.popularmovies.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gemma.popularmovies.domain.model.Movie

/**
 * Composables related to movies that can be used in different screens
 */

@Composable
fun ReleaseDate(movie: Movie?, tint: Color) {
    Row(horizontalArrangement = Arrangement.Center) {
        Icon(
            Icons.Filled.DateRange,
            contentDescription = "${movie?.release_date}",
            tint = tint
        )
        Text(
            "${movie?.release_date}",
            Modifier.padding(start = 8.dp),
            color = tint
        )
    }
}

@Composable
fun MovieRating(movie: Movie?, tint:Color) {
    Row(horizontalArrangement = Arrangement.Center) {
        Icon(
            Icons.Filled.Star,
            contentDescription = "${movie?.rating}",
            tint = tint
        )
        Text(
            "${movie?.rating} / 10",
            Modifier.padding(start = 8.dp),
            color = tint
        )
    }
}