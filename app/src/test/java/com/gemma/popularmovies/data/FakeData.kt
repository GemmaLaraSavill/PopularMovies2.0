package com.gemma.popularmovies.data

import com.gemma.popularmovies.data.cache.model.CachedMovieMinimal
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Provider
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.model.Trailer

class FakeData {

    val extraTrailer = Trailer(
        566525, "60d52b3cc1606a007e706b2b",
        "8YjFbMbfXaQ",
        "YouTube",
        "Official Trailer"
    )

    val extraMovie = Movie(
        566525,
        "Shang-Chi and the Legend of the Ten Rings",
        "https://image.tmdb.org/t/p/w185/1BIoJGKbXjdFDAqUEiA2VHqkK1Z.jpg",
        "https://image.tmdb.org/t/p/w185/nDLylQOoIazGyYuWhk21Yww5FCb.jpg",
        "Shang-Chi must confront the past he thought he left behind when he is drawn into the web of the mysterious Ten Rings organization.",
        "7.7",
        "2021-09-01",
        0,
        extraTrailer,
        1
    )

    fun getMovieById(): Movie {
        return extraMovie
    }


    fun getMovieList(): MutableList<Movie> {
        return mutableListOf(
            Movie(
                524434,
                "Eternals",
                "https://image.tmdb.org/t/p/w185/6AdXwFTRTAzggD2QUTt5B7JFGKL.jpg",
                backdrop = "https://image.tmdb.org/t/p/w185/nDLylQOoIazGyYuWhk21Yww5FCb.jpg",
                "The Eternals are a team of ancient aliens who have been living on Earth in secret for thousands of years. When an unexpected tragedy forces them out of the shadows, they are forced to reunite against mankind’s most ancient enemy, the Deviants.",
                "7.1",
                "2021-11-03",
                0,
                null,
                1
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
                null,
                1
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
                null,
                1
            )
        )
    }

    fun getCachedMovieMinimalList(): List<CachedMovieMinimal> {
        return listOf(
            CachedMovieMinimal(
                0,
                "title 1",
                "https://image.tmdb.org/t/p/w185/2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg",
                0,
                1

            ),
            CachedMovieMinimal(
                0,
                "title 2",
                "https://image.tmdb.org/t/p/w185/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
                0,
                1
            ),
            CachedMovieMinimal(
                0,
                "title 3",
                "https://image.tmdb.org/t/p/w185/d5NXSklXo0qyIYkgV94XAgMIckC.jpg",
                0,
                1
            )
        )
    }

    fun getMovieCast(): List<Role> {
        return listOf(
            Role(
                10859,
                512195,
                "Ryan Reynolds",
                "https://image.tmdb.org/t/p/w154/4SYTH5FdB0dAORV98Nwg3llgVnY.jpg",
                "Nolan Booth"
            ),
            Role(
                90633,
                512195,
                "Gal Gadot",
                "https://image.tmdb.org/t/p/w154/fysvehTvU6bE3JgxaOTRfvQJzJ4.jpg",
                "Sarah Black / The Bishop"
            ),
            Role(
                1742596,
                512195,
                "Ritu Arya",
                "https://image.tmdb.org/t/p/w154/9ojwd1UhOJx6Jx8s6hLwNP2HwGb.jpg",
                "Inspector Urvashi Das"
            )
        )
    }

    fun getNetworkMovieCast(): List<Role> {
        // cast for Shang-Chi and the Legend of the Ten Rings
        val shangChiCastList = listOf<Role>(
            Role(
                1489211,
                566525,
                "Simu Liu",
                "https://image.tmdb.org/t/p/w154/zrJjYjOYzDj7eY9oiHAoz8Yh0yk.jpg",
                "Shaun / Shang-Chi"
            ),
            Role(
                90633,
                566525,
                "Tony Leung Chiu-wai",
                "https://image.tmdb.org/t/p/w154/nQbSQAws5BdakPEB5MtiqWVeaMV.jpg",
                "Xu Wenwu / The Mandarin"
            ),
            Role(
                1742596,
                566525,
                "Awkwafina",
                "https://image.tmdb.org/t/p/w154/l5AKkg3H1QhMuXmTTmq1EyjyiRb.jpg",
                "Katy Chen"
            )
        )
        return shangChiCastList
    }

    fun getProviderList(): List<Provider> {
        return listOf(
            Provider(
                337,
                497698,
                "Disney Plus",
                "https://image.tmdb.org/t/p/w154/mIGNMa4QE8E5fIABDH6lWfy5qhn.jpg",
                "flatrate"
            ),
            Provider(
                10,
                497698,
                "Amazon Video",
                "https://image.tmdb.org/t/p/w154/sVBEF7q7LqjHAWSnKwDbzmr2EMY.jpg",
                "buy"
            ),
            Provider(
                2,
                497698,
                "Apple iTunes",
                "https://image.tmdb.org/t/p/w154/q6tl6Ib6X5FT80RMlcDbexIo4St.jpg",
                "rent"
            )
        )
    }
}