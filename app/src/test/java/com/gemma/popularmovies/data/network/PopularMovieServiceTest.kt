package com.gemma.popularmovies.data.network

import com.gemma.popularmovies.data.network.model.MoviesPageDto
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class PopularMovieApiTestUsingMockServer {

    @get:Rule
    val mockWebServer = MockWebServer()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val popularMovieService by lazy {
        retrofit.create(MoviesApiService::class.java)
    }


    /**
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */
    private fun getJson(path: String): String {
        val uri = this.javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    private val testPopularMoviesJson = getJson("popular_movies_list.json")

    @Test
    fun mockWebserver_EmitsMovies() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testPopularMoviesJson)
                .setResponseCode(200)
        )

        runBlocking {
            val jsonResult: MoviesPageDto = popularMovieService.getPopularMovies(1)
            assertEquals(20, jsonResult.movieList.count())
        }
    }

    @Test
    fun apiGetPopularMovieList_returnsPopularMovies() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testPopularMoviesJson)
                .setResponseCode(200)
        )
        runBlocking {
            val moviesFromApi: MoviesPageDto = popularMovieService.getPopularMovies(1)
            assertEquals(20, moviesFromApi.movieList.count())
            assertEquals("Venom: Let There Be Carnage", moviesFromApi.movieList[0].title)
            assertEquals("/xmbU4JTUm8rsdtn7Y3Fcm30GpeT.jpg", moviesFromApi.movieList[1].poster)
        }
    }

    // TRAILERS
    private val testMovieId = 497698
    private val testVideosJson = getJson("videos.json")

    @Test
    fun mockWebserver_EmitsVideo() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testVideosJson)
                .setResponseCode(200)
        )

        runBlocking {
            val jsonResult = popularMovieService.getVideos(testMovieId)
            assertEquals(42, jsonResult.videoList.count())
        }
    }

    @Test
    fun apiGetVideos_returnsVideos() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testVideosJson)
                .setResponseCode(200)
        )
        runBlocking {
            val trailersFromApi = popularMovieService.getVideos(566525)
            println(trailersFromApi.toString())
            assertEquals(42, trailersFromApi.videoList.count())
            assertEquals("61612683b865eb0061c2ee80", trailersFromApi.videoList[0].id)
            assertEquals("Return", trailersFromApi.videoList[0].name)
        }
    }

    // CAST
    private val testCreditsJson = getJson("credits.json")

    @Test
    fun mockWebserver_EmitsCast() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testCreditsJson)
                .setResponseCode(200)
        )

        runBlocking {
            val creditsResult = popularMovieService.getCredits(testMovieId)
            assertEquals(1245, creditsResult.roleList[0].artist_id)
            assertEquals("Natasha Romanoff / Black Widow", creditsResult.roleList[0].character)
            assertEquals("Scarlett Johansson", creditsResult.roleList[0].name)
            assertEquals("/mODcczqQyKuphfFAoBZGhxgnNfs.jpg", creditsResult.roleList[0].image)
        }
    }

    // PROVIDERS
    private val testProvidersJson = getJson("providers.json")

    @Test
    fun mockWebserver_EmitsProviders() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testProvidersJson)
                .setResponseCode(200)
        )

        runBlocking {
            val jsonResult = popularMovieService.getProviders(testMovieId)
//            println(jsonResult.countries.country.link)
//            println(jsonResult.countries.country.flatRateProviders.first().name)
            assertEquals("https://www.themoviedb.org/movie/497698-black-widow/watch?locale=ES", jsonResult.countries.country.link)
            assertEquals(2, jsonResult.countries.country.flatRateProviders.count())
            assertEquals(4, jsonResult.countries.country.rentProviders.count())
            assertEquals(5, jsonResult.countries.country.buyProviders.count())
            assertEquals("Disney Plus", jsonResult.countries.country.flatRateProviders.first()?.name)
        }
    }


}

