package ru.dmitry.moviesearch.model

data class MovieRecyclerViewItemData(
    var movie: MovieBrief,
    var liked: Boolean = false,
    var bookmarked: Boolean = false
)