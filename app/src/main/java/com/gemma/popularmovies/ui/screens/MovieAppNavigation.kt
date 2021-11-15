package com.gemma.popularmovies.ui.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gemma.popularmovies.ui.screens.detail.DetailScreen
import com.gemma.popularmovies.ui.screens.detail.MovieViewModel
import com.gemma.popularmovies.ui.screens.main.MainScreen
import com.gemma.popularmovies.ui.screens.main.MovieListViewModel

@ExperimentalMaterialApi
@Composable
fun MovieApp() {
    var navController = rememberNavController()
    val listViewModel: MovieListViewModel = viewModel()
    val movieDetailViewModel: MovieViewModel = viewModel()
    NavHost(navController = navController, startDestination = MovieAppScreen.MainScreen.route) {
        composable(route = MovieAppScreen.MainScreen.route) {
            MainScreen(navController = navController, viewModel = listViewModel)
        }
        composable(
            route = MovieAppScreen.DetailScreen.route + "/{movie_id}",
            arguments = listOf(
                navArgument("movie_id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {
            entry ->
            DetailScreen(entry.arguments?.getInt("movie_id"), navController = navController, viewModel = movieDetailViewModel)
        }
    }
}

