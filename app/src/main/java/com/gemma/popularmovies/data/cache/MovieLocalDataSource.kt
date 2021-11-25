package com.gemma.popularmovies.data.cache

import com.gemma.popularmovies.data.MovieDataSource
import com.gemma.popularmovies.data.cache.daos.CastDao
import com.gemma.popularmovies.data.cache.daos.MovieDao
import com.gemma.popularmovies.data.cache.model.CachedArtist
import com.gemma.popularmovies.data.cache.model.CachedMovie
import com.gemma.popularmovies.data.cache.model.CachedRole
import com.gemma.popularmovies.data.cache.model.CachedTrailer
import com.gemma.popularmovies.data.network.ApiConstants
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Manages data in and out the DB (local cache = source of truth for this app)
 */
class MovieLocalDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val castDao: CastDao
) : MovieDataSource {

    override suspend fun getPopularMovies(): Flow<List<Movie>> {
        return movieDao.getMostPopularMovies().map { movieList ->
            movieList
                .map {
                    it.poster = ApiConstants.thumbUrl + ApiConstants.thumbSize185 + it.poster
                    it.toDomain()
                }
        }
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        return movieDao.getFullMovieData(movieId)
            .map {
                it?.movie?.poster =
                    ApiConstants.thumbUrl + ApiConstants.thumbSize185 + it?.movie?.poster
                it?.movie?.backdrop =
                    ApiConstants.thumbUrl + ApiConstants.thumbSize500 + it?.movie?.backdrop
                it?.trailer
                it?.movie?.toDomain(it.trailer?.toDomain())
            }
    }

    override suspend fun insertMovie(fullMovieData: Flow<Movie?>) {
        fullMovieData.map {
            if (it !== null) {
                movieDao.insertMovie(it.toCache())
            }
        }
    }

    override suspend fun toggleFavorite(movieId: Int) {
        movieDao.toggleFavorite(movieId)
    }

    override suspend fun getTrailer(movieId: Int): Flow<Trailer?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTrailer(movieId: Int, trailer: Trailer?) {
        if (trailer !== null) {
            val cachedTrailer = CachedTrailer(
                movieId,
                trailer.trailer_id,
                trailer.key,
                trailer.site,
                trailer.name
            )
            movieDao.insertTrailer(cachedTrailer)
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { favList ->
            favList.map {
                it.poster = ApiConstants.thumbUrl + ApiConstants.thumbSize185 + it.poster
                it.toDomain(null)
            }
        }
    }

    /**
     * Insert popular movies to local data source
     */
    override suspend fun insertFreshPopularMovies(popularMovies: List<Movie>) {

        val popularMoviesToDb = mutableListOf<CachedMovie>()

        // convert API data to DB format
        var cachedMovie: CachedMovie
        for (movie in popularMovies) {
            cachedMovie = CachedMovie(
                movie.movie_id,
                movie.title,
                movie.poster,
                movie.backdrop,
                movie.overview,
                movie.rating,
                movie.release_date,
                0
            )
            popularMoviesToDb.add(cachedMovie)
        }
        movieDao.insertFreshMovies(popularMoviesToDb)
    }


    override suspend fun getFreshPopularMovies(): List<Movie> {
        // Not required for the local data source = source of truth
        return emptyList()
    }

    override suspend fun getMovieCast(movieId: Int): Flow<List<Role?>> {
        return castDao.getMovieCast(movieId).map { castList ->
            castList.map {
                it?.artist?.image =
                    ApiConstants.thumbUrl + ApiConstants.thumbSize154 + it?.artist?.image
                it?.toDomain()
            }
        }
    }

    override suspend fun getFreshMovieCast(movieId: Int): List<Role?> {
        // Not required for the local data source = source of truth
        return emptyList()
    }

    /**
     * Api sends a list of roles, for the local cache we will save the
     * artists and roles separately as one artist can have different roles in different movies
     */
    override suspend fun insertCast(roleList: List<Role?>) {
        // for the DB
        val rolesToDb = mutableListOf<CachedRole>()
        val artistsToDb = mutableListOf<CachedArtist>()

        // convert API data to DB format
        var cachedRole: CachedRole
        var cachedArtist: CachedArtist
        for (role in roleList) {
            if (role != null) {
                cachedRole = CachedRole(
                    0,
                    role.artist_id,
                    role.movie_id,
                    role.character
                )
                cachedArtist = CachedArtist(
                    0,
                    role.artist_id,
                    role.name,
                    role.image_path
                )
                rolesToDb.add(cachedRole)
                artistsToDb.add(cachedArtist)
            }
        }

        // insert into DB
        castDao.insertArtists(artistsToDb)
        castDao.insertRoles(rolesToDb)
    }

}