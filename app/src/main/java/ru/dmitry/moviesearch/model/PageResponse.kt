package ru.dmitry.moviesearch.model

data class PageResponse<T>(
    val page: Int,
    val results: List<T>,
    val total_pages: Int,
    val total_results: Int
)