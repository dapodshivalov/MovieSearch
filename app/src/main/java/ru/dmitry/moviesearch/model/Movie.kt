package ru.dmitry.moviesearch.model

data class Movie(
    var title: String,
    var year: Int,
    var rating: Double,
    var posterUrl: String
)