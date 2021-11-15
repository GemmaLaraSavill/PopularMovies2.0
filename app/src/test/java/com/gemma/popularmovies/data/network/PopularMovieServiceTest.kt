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
            var jsonResult: MoviesPageDto = popularMovieService.getPopularMovies(1)
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
            var moviesFromApi: MoviesPageDto = popularMovieService.getPopularMovies(1)
            assertEquals(20, moviesFromApi.movieList.count())
            assertEquals("Venom: Let There Be Carnage", moviesFromApi.movieList.get(0).title)
            assertEquals("/xmbU4JTUm8rsdtn7Y3Fcm30GpeT.jpg", moviesFromApi.movieList.get(1).poster)
        }
    }

    // TRAILERS
    val testMovieId = 497698;
    private val testVideosJson = getJson("videos.json")

    @Test
    fun mockWebserver_EmitsVideo() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testVideosJson)
                .setResponseCode(200)
        )

        runBlocking {
            var jsonResult = popularMovieService.getVideos(testMovieId)
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
            var trailersFromApi = popularMovieService.getVideos(566525)
            println(trailersFromApi.toString())
            assertEquals(42, trailersFromApi.videoList.count())
            assertEquals("61612683b865eb0061c2ee80", trailersFromApi.videoList.get(0).id)
            assertEquals("Return", trailersFromApi.videoList.get(0).name)
        }
    }

}

