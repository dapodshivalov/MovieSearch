package ru.dmitry.moviesearch.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.dmitry.moviesearch.model.MovieBrief
import ru.dmitry.moviesearch.model.PageResponse


interface Service {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String?,
        @Query("page") pageIndex: Int,
        @Query("language") language: String = "ru"
    ): Call<PageResponse<MovieBrief>>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String?,
        @Query("page") pageIndex: Int,
        @Query("language") language: String = "ru"
    ): Call<PageResponse<MovieBrief>>

    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") apiKey: String?,
        @Query("page") pageIndex: Int,
        @Query("query") query: String,
        @Query("language") language: String = "ru"
    ): Call<PageResponse<MovieBrief>>
}