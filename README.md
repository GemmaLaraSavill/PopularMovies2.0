# Popular Movies 2.0 (Not Actively Maintained) 

Welcome to Popular Movies 2.0! 

This Android app showcases popular movies using themoviedb.org API. 

It's designed for educational and demonstration purposes, not for commercial use.

## Key Features

**Modern Android Development:** This app utilizes the latest Android development practices, including:
 - **Kotlin:** Leverages a modern and concise programming language for development.
 - **Room:** Employs a powerful library for managing offline data storage with an internal SQLite database.
- **Jetpack Compose:** Delivers a declarative and efficient way to build user interfaces.
- **Kotlin Coroutines and Flow:** Enables asynchronous data fetching without blocking the UI thread.
- **Hilt:** Implements dependency injection for simplified code management.
- **MVVM Architecture:** Separates concerns for better maintainability.
- **Jetpack Paging 3:** Optimizes data loading for large datasets.

**Offline Support:** Users can access cached movie data even when offline.

**Testing:** Unit tests ensure code quality and reliability (using AndroidJUnit4 and Okhttp3 MockWebServer).

## Background
This project originated as [Popular Movies](https://github.com/GemmaLaraSavill/PopularMovies) for the **Udacity Android Developer Nanodegree in 2016**. 

Recognizing the significant advancements in Android development, a new version was created leveraging the latest tools and best practices. 

**Future Development:**

While this repository is no longer actively maintained, the code serves as a valuable learning resource. 

**Note:** This project is for demonstration and educational purposes only and not intended for commercial use.

## Highlights

Screenshot of one the Composables in the app in Android Studio Preview mode:
![Preview of Movie detail screen body](./github/movie-detail-screen-UI-compose-preview.png)

Preview video of the detail screen of the app, now showing movie cast:

<img src="./github/app-preview-with-cast-and-providers.gif" width="320" />

With Paging 3 Jetpack library user gets smooth infinite scrolling as we anticipate getting the pages from the server into cache (no waiting around for Movies to load) :

<img src="./github/paging-3-db-and-network-manager.gif" />

##### Here you can see how scrolling is triggering new pages being added to the local database (source-of-truth) and the Network Manager tab showing the transmission peaks (with some of delay) as the app gets the movies from the API



<hr />




## Installation

1. Clone the code and import it as a project into [Android Studio](https://developer.android.com/studio)

2. Go to the [themoviedb.org](http://themoviedb.org) and request an API key. 

3. Once you have it open
_/app/src/main/java/com/gemma/popularmovies/data/network/ApiConstants.kt_ 
file and add your own API key in line 32
`const val API_KEY = ""`
