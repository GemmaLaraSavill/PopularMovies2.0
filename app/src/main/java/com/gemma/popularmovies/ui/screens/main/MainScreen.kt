package com.gemma.popularmovies.ui.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.gemma.popularmovies.R
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.ui.composables.EmptyListWarning
import com.gemma.popularmovies.ui.composables.LoadingIndicator
import com.gemma.popularmovies.ui.composables.MovieRating
import com.gemma.popularmovies.ui.composables.ReleaseDate
import com.gemma.popularmovies.ui.screens.MovieAppScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class ListType {
    Popular, Favorite
}


@ExperimentalMaterialApi
@Composable
fun MainScreen(navController: NavController, viewModel: MovieListViewModel) {

    // state
    val movieList: List<Movie> by viewModel.movieList.collectAsState(initial = emptyList())

    val favMovieList: List<Movie> by viewModel.favoriteMovieList.collectAsState(initial = emptyList())

    var listDisplayed by rememberSaveable { mutableStateOf(ListType.Popular) }

    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            PopularMoviesTopAppBar(
                listDisplayed,
                onFavoritesActionClicked = { listDisplayed = it },
                scaffoldState,
                snackbarCoroutineScope
            )
        },
    ) { innerPadding ->
        BodyContent(
            Modifier.padding(innerPadding),
            listDisplayed,
            movieList,
            favMovieList,
            navController,
            reload = { viewModel.reloadMovies() }
        )
    }
}

@Composable
fun PopularMoviesTopAppBar(
    listDisplayed: ListType,
    onFavoritesActionClicked: (listDisplayed: ListType) -> Unit,
    scaffoldState: ScaffoldState, snackbarCoroutineScope: CoroutineScope
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {
                snackbarCoroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("TODO implement Navigation Drawer")
                }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            if (listDisplayed == ListType.Popular) {
                IconButton(onClick = { onFavoritesActionClicked(ListType.Favorite) }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favorite movies")
                }
            } else {
                IconButton(onClick = { onFavoritesActionClicked(ListType.Popular) }) {
                    Icon(Icons.Filled.Movie, contentDescription = "Popular movies")
                }
            }
            IconButton(onClick = {
                snackbarCoroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("TODO implement search")
                }
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search movies")
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
fun BodyContent(
    modifier: Modifier,
    listDisplayed: ListType,
    moviePosterList: List<Movie>,
    favoriteMovieList: List<Movie>,
    navController: NavController,
    reload: () -> Unit
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .fillMaxHeight()
    ) {
        if (listDisplayed == ListType.Popular) {
            if (moviePosterList.isEmpty()) {
                EmptyListWarning(
                    modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .align(Alignment.Center),
                    message = stringResource(R.string.noMoviesFound),
                    actionText = stringResource(R.string.tryAgain),
                    onButtonClick = { reload() })
            } else {
                PosterGrid(
                    moviePosterList,
                    onPosterClick = { navController.navigate(MovieAppScreen.DetailScreen.withArgs(it)) }
                )
            }
        } else {
            Box(
                modifier = Modifier.padding(4.dp)
            ) {
                FavoritePosterList(
                    favoriteMovieList,
                    onPosterClick = { navController.navigate(MovieAppScreen.DetailScreen.withArgs(it)) }
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PosterGrid(movies: List<Movie>, onPosterClick: (Int) -> Unit) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2)
    ) {
        items(items = movies) { movie ->
            PosterItem(movie.poster!!, movie.title, movie.movie_id, onPosterClick)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun PosterItem(poster: String?, title: String?, movie_id: Int, onPosterClick: (Int) -> Unit) {
    val posterPainter = rememberImagePainter(
        data = poster,
        builder = { size(OriginalSize) })
    Box(
        Modifier
            .border(width = 1.dp, color = MaterialTheme.colors.secondary)
            .height(278.dp)
            .width(185.dp)
    ) {
        Image(
            painter = posterPainter,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    onPosterClick(movie_id)
                })
        )
    }
    var imageState = posterPainter.state
    if (imageState is ImagePainter.State.Loading) {
        Box(
            Modifier
                .height(278.dp)
                .width(185.dp),
            Alignment.Center
        ) {
            LoadingIndicator()
        }
    }
    if (imageState is ImagePainter.State.Error) {
        Box(
            Modifier
                .height(278.dp)
                .width(185.dp)
                .border(
                    1.dp, MaterialTheme.colors.secondary.copy(alpha = 0.1F)
                ),
            Alignment.Center
        ) {
            Icon(
                Icons.Default.MovieCreation,
                title,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center),
                tint = Color.Black.copy(alpha = 0.2F)
            )
            if (title != null) {
                Text(
                    title,
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun FavoritePosterList(movies: List<Movie>, onPosterClick: (Int) -> Unit) {
    if (movies.count() > 0) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(movies) { movie ->
                FavoriteMovieCard(movie, onPosterClick)
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.noFavMoviesFound),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun FavoriteMovieCard(movie: Movie, onPosterClick: (Int) -> Unit) {
    Card(
        onClick = { onPosterClick(movie.movie_id) },
        elevation = 4.dp
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Image(
                painter = rememberImagePainter(
                    data = movie.poster,
                    builder = {
                        size(OriginalSize)
                        placeholder(R.drawable.poster_loading)
                        error(R.drawable.poster_error)
                    },
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.weight(1F)
            )
            Column(
                Modifier
                    .weight(1F)
                    .padding(16.dp), Arrangement.Bottom
            ) {
                Text(
                    "${movie.title}",
                    modifier = Modifier.padding(bottom = 32.dp),
                    style = MaterialTheme.typography.h3
                )
                Box(Modifier.padding(bottom = 16.dp)) {
                    ReleaseDate(movie, MaterialTheme.colors.onSurface.copy(alpha = 0.6F))
                }
                Box(Modifier.padding(bottom = 8.dp)) {
                    MovieRating(movie, MaterialTheme.colors.onSurface.copy(alpha = 0.6F))
                }
            }
        }
    }
}
