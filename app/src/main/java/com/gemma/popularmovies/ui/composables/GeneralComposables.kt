package com.gemma.popularmovies.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Composables that can be reused and are not project specific
 */

@Composable
fun LoadingIndicator() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun LoadingPreview() {
    LoadingIndicator()
}

@Composable
fun EmptyListWarning(modifier: Modifier, message: String, actionText: String, onButtonClick:() -> Unit) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            message,
            Modifier
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.body1
        )
        Button(
            onClick = {
                onButtonClick()
            },
            Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(actionText)
        }
    }
}

@Preview
@Composable
fun EmptyListPreview() {
    val modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    EmptyListWarning(modifier, "Message to user", "Try again", onButtonClick = {})
}