package com.gemma.popularmovies.data

import com.gemma.popularmovies.data.cache.model.CachedMovieMinimal
import com.gemma.popularmovies.domain.model.Movie

class FakeData {

    var mutableMovie = Movie(
        566525,
        "Shang-Chi and the Legend of the Ten Rings",
        "https://image.tmdb.org/t/p/w185/1BIoJGKbXjdFDAqUEiA2VHqkK1Z.jpg",
        backdrop = "https://image.tmdb.org/t/p/w500/nDLylQOoIazGyYuWhk21Yww5FCb.jpg",
        "Shang-Chi must confront the past he thought he left behind when he is drawn into the web of the mysterious Ten Rings organization.",
        "7.7",
        "2021-09-01",
        0,
        null
    )

    fun getMovieById(): Movie {
        return mutableMovie
    }


    fun getMovieList(): List<Movie> {
        return listOf(
            Movie(
                524434,
                "Eternals",
                "https://image.tmdb.org/t/p/w185/6AdXwFTRTAzggD2QUTt5B7JFGKL.jpg",
                backdrop = "https://image.tmdb.org/t/p/w185/nDLylQOoIazGyYuWhk21Yww5FCb.jpg",
                "The Eternals are a team of ancient aliens who have been living on Earth in secret for thousands of years. When an unexpected tragedy forces them out of the shadows, they are forced to reunite against mankind’s most ancient enemy, the Deviants.",
                "7.1",
                "2021-11-03",
                0,
                null
            ),
            Movie(
                0,
                "Venom: Let There Be Carnage",
                "https://image.tmdb.org/t/p/rjkmN1dniUHVYAtwuV3Tji7FsDO.jpg",
                backdrop = "https://image.tmdb.org/t/p/8Y43POKjjKDGI9MH89NW0NAzzp8.jpg",
                null,
                null,
                null,
                1,
                null
            ),
            Movie(
                0,
                null,
                "https://image.tmdb.org/t/p/w185/d5NXSklXo0qyIYkgV94XAgMIckC.jpg",
                backdrop = "https://image.tmdb.org/t/p/w185/nDLylQOoIazGyYuWhk21Yww5FCb.jpg",
                null,
                null,
                null,
                0,
                null
            )
        )
    }

    fun getCachedMovieMinimalList(): List<CachedMovieMinimal> {
        return listOf(
            CachedMovieMinimal(
                0,
                "https://image.tmdb.org/t/p/w185/2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg",
                0

                ),
            CachedMovieMinimal(
                0,
                "https://image.tmdb.org/t/p/w185/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
0
                ),
            CachedMovieMinimal(
                0,
                "https://image.tmdb.org/t/p/w185/d5NXSklXo0qyIYkgV94XAgMIckC.jpg",
0
                )
        )
    }
}