package com.gemma.popularmovies.data.cache

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gemma.popularmovies.data.MovieDataSource
import com.gemma.popularmovies.data.cache.daos.CastDao
import com.gemma.popularmovies.data.cache.daos.MovieDao
import com.gemma.popularmovies.data.cache.daos.ProviderDao
import com.gemma.popularmovies.data.cache.model.*
import com.gemma.popularmovies.data.network.ApiConstants
import com.gemma.popularmovies.data.network.MovieRemoteMediator
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Provider
import com.gemma.popularmovies.domain.model.Role
import com.gemma.popularmovies.domain.model.Trailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Manages data in and out the DB (local cache = source of truth for this app)
 */
class MovieLocalDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val castDao: CastDao,
    private val providerDao: ProviderDao
) : MovieDataSource {


    @ExperimentalPagingApi
    override suspend fun getPagedMovies(moviesRemoteMediator: MovieRemoteMediator): Flow<PagingData<CachedMovieMinimal>> {
        // setup the pager
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            remoteMediator = moviesRemoteMediator,
            pagingSourceFactory = {
                // source-of-truth
                movieDao.getMoviesPagingSource()
            }
        ).flow
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { favList ->
            favList.map {
                it.poster = ApiConstants.thumbUrl + ApiConstants.thumbSize185 + it.poster
                it.toDomain(null)
            }
        }
    }

    override suspend fun getFreshPopularMovies(page: Int): List<Movie> {
        // Not required for the local data source = source of truth
        return emptyList()
    }

    override suspend fun addFreshPopularMovies(movies: List<Movie>) {
        // for the DB
        val popularMoviesToDb = mutableListOf<CachedMovie>()

        // convert API data to DB format
        var cachedMovie: CachedMovie
        for (movie in movies) {
            cachedMovie = CachedMovie(
                movie.movie_id,
                movie.title,
                movie.poster,
                movie.backdrop,
                movie.overview,
                movie.rating,
                movie.release_date,
                0,
                movie.page
            )
            popularMoviesToDb.add(cachedMovie)
        }
        movieDao.insertNewMovies(popularMoviesToDb)
    }

    override suspend fun countMovies(): Int {
        return movieDao.countMovies()
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
        // Not required for the local data source as the movie is extracted
        // in getMovieById() as a CachedMovieAggregate that holds movie and trailer
        return emptyFlow();
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



    /**
     * Refresh popular movies in local data source
     */
    override suspend fun refreshMovies(popularMovies: List<Movie>) {
        // for the DB
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
                0,
                movie.page
            )
            popularMoviesToDb.add(cachedMovie)
        }
        movieDao.refreshMovies(popularMoviesToDb)
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

    override suspend fun getProviders(movieId: Int): Flow<List<Provider?>> {
        return providerDao.getMovieProviders(movieId).map { providerList ->
            providerList.onEach {
            }.map {
                it?.provider?.logo =
                    ApiConstants.thumbUrl + ApiConstants.thumbSize154 + it?.provider?.logo
                it?.toDomain()
            }
        }
    }

    override suspend fun getFreshProviders(movieId: Int): List<Provider?> {
        // Not required for the local data source = source of truth
        return emptyList()
    }

    /**
     * Api sends a list of providers, for the local cache we will save the
     * providers and roles separately as one artist can have different roles in different movies
     */
    override suspend fun insertProviders(providerList: List<Provider?>) {
        // for the DB
        val providersToDb = mutableListOf<CachedProvider>()
        val aMovieProvidersToDb = mutableListOf<CachedProviderForMovie>()

        // convert API data to DB format
        var cachedProvider: CachedProvider
        var cachedProviderForMovie: CachedProviderForMovie
        for (provider in providerList) {
            if (provider != null) {
                cachedProvider = CachedProvider(provider.provider_id, provider.name, provider.logo)
                cachedProviderForMovie = CachedProviderForMovie(
                    0,
                    provider.movie_id,
                    provider.provider_id,
                    provider.type
                )
                providersToDb.add(cachedProvider)
                aMovieProvidersToDb.add(cachedProviderForMovie)
            }
        }
        // insert into DB
        providerDao.insertProviders(providersToDb)
        providerDao.insertProvidersForMovie(aMovieProvidersToDb)
    }


}