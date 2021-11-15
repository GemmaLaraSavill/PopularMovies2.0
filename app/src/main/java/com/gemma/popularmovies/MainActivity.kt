package com.gemma.popularmovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import com.gemma.popularmovies.ui.screens.MovieApp
import com.gemma.popularmovies.ui.theme.PopularMoviesTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Popular Movies 2.0
 *
 * An app that shows movies from themovieDB.org
 *
 * User can view a list of popular movies or a selection of favorites the user can keep in the local memory via the favorite button
 *
 * Each movie shows details such as overview and a trailer, when available
 *
 * To minimize data usage the app will refresh data from the server only when new data is available (7 days has gone by since last data refresh). Favorite movies will always be available on local storage.
 *
 * By Gemma Lara Savill November 2021, this app is a remake of my previous app Popular Movies task for my Udacity Android Developer Nanodegree, with all the modern Android development techniques.
 *
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PopularMoviesTheme {
                MovieApp()
            }
        }
    }

}