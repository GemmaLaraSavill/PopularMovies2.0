package com.gemma.popularmovies.ui.screens

sealed class MovieAppScreen(val route:String) {
    object MainScreen: MovieAppScreen("main_screen")
    object DetailScreen: MovieAppScreen("detail_screen")

    fun withArgs(vararg args:Int): String {
        return buildString {
            append(route)
                args.forEach {
                    arg -> append("/$arg")
                }
        }
    }
}
