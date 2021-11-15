package com.gemma.popularmovies.ui.screens.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.gemma.popularmovies.R
import com.gemma.popularmovies.domain.model.Movie
import com.gemma.popularmovies.domain.model.Trailer
import com.gemma.popularmovies.ui.composables.LoadingIndicator
import com.gemma.popularmovies.ui.composables.MovieRating
import com.gemma.popularmovies.ui.composables.ReleaseDate
import com.gemma.popularmovies.ui.theme.PopularMoviesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(movieId: Int?, navController: NavController, viewModel: MovieViewModel) {

    viewModel.getMovieById(movieId)
    val movie: Movie? by viewModel.movie.collectAsState(null)

    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            DetailTopAppBar(
                { viewModel.onFavoriteActionClicked(movie?.movie_id) },
                movie?.favorite,
                navController,
                scaffoldState,
                snackbarCoroutineScope
            )
        }) { innerPadding ->
        DetailScreenBody(Modifier.padding(innerPadding), movie)
    }
}


@Composable
fun DetailScreenBody(modifier: Modifier, movie: Movie?) {
    val scrollState = rememberScrollState()
    PopularMoviesTheme {
        if (movie == null) {
            LoadingIndicator()
        } else {
            Column(modifier = modifier.verticalScroll(scrollState)) {
                BackDrop(movie)
                TitleRow(movie)
                Text(
                    "${movie.overview}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                if (movie.trailer !== null) {
                    Trailer(movie.trailer!!, movie.poster)
                }
            }
        }
    }
}

@Composable
fun Trailer(trailer: Trailer, poster: String?) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Card(Modifier.fillMaxWidth(), elevation = 10.dp) {
            Row {
                Box(
                    Modifier
                        .width(135.dp),
                    Alignment.Center
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = poster,
                            builder = {
                                size(OriginalSize)
                                placeholder(R.drawable.test_poster)
                            },
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.background.copy(alpha = 0.5F))
                    )
                    IconButton(onClick = { openYouTubeVideo(trailer, context) }) {
                        Icon(
                            Icons.Default.PlayArrow,
                            stringResource(R.string.viewVideo),
                            modifier = Modifier.size(96.dp)
                        )
                    }
                }
                Column(
                    Modifier
                        .padding(start = 32.dp, end = 32.dp)
                        .height(IntrinsicSize.Max)
                ) {
                    Text(
                        stringResource(R.string.trailer),
                        Modifier.padding(top = 16.dp, bottom = 32.dp),
                        style = MaterialTheme.typography.h2
                    )
                    Text(
                        "${trailer.name}",
                        Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colors.secondary.copy(alpha = 0.8F),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

/**
 * Opens the YouTube app to watch the trailer
 */
fun openYouTubeVideo(trailer: Trailer, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://${trailer.key}"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

@Preview
@Composable
fun PreviewTrailer() {
    PopularMoviesTheme {
        Trailer(testTrailer, testMovie.poster)
    }
}

@Composable
fun BackDrop(movie: Movie?) {
    Image(
        painter = rememberImagePainter(
            data = movie?.backdrop,
            builder = {
                size(OriginalSize)
                placeholder(R.drawable.test_backdrop)
            },
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun TitleRow(movie: Movie?) {
    Row(Modifier.background(MaterialTheme.colors.secondary)) {
        Column(
            Modifier
                .weight(6F, true)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${movie?.title}",
                style = MaterialTheme.typography.h1,
                modifier = Modifier
                    .padding(10.dp),
                color = MaterialTheme.colors.onSecondary,
                textAlign = TextAlign.Center
            )
            Row(Modifier.padding(top = 8.dp, bottom = 16.dp)) {
                Column(
                    Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ReleaseDate(movie, MaterialTheme.colors.onSecondary.copy(alpha = 0.6f))
                }
                Column(
                    Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MovieRating(movie, MaterialTheme.colors.onSecondary.copy(alpha = 0.6f))
                }
            }
        }
    }
}

@Preview(
    "Detail Screen Body",
//    showSystemUi = true,
//    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewDetailScreenBody() {
    PopularMoviesTheme {
        DetailScreenBody(Modifier.padding(8.dp), testMovie)
    }
}


@Composable
fun DetailTopAppBar(
    toggleFavorite: () -> Unit,
    favorite: Int?,
    navController: NavController,
    scaffoldState: ScaffoldState,
    snackbarCoroutineScope: CoroutineScope
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back to list")
            }
        },
        actions = {
            IconButton(onClick = toggleFavorite) {
                var favIcon = Icons.Default.FavoriteBorder
                if (favorite == 1) {
                    favIcon = Icons.Filled.Favorite
                }
                Icon(
                    imageVector = favIcon,
                    contentDescription = "Localized description"
                )
            }
            IconButton(onClick = {
                snackbarCoroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("TODO implement share movie")
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share"
                )
            }
        }
    )
}

val testTrailer = Trailer(
    566525, "60d52b3cc1606a007e706b2b",
    "8YjFbMbfXaQ",
    "YouTube",
    "Official Trailer"
)

val testMovie = Movie(
    566525,
    "Shang-Chi and the Legend of the Ten Rings",
    "https://image.tmdb.org/t/p/w185/1BIoJGKbXjdFDAqUEiA2VHqkK1Z.jpg",
    "https://image.tmdb.org/t/p/w185/nDLylQOoIazGyYuWhk21Yww5FCb.jpg",
    "Shang-Chi must confront the past he thought he left behind when he is drawn into the web of the mysterious Ten Rings organization.",
    "7.7",
    "2021-09-01",
    0,
    testTrailer
)