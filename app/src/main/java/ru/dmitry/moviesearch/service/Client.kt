package ru.dmitry.moviesearch.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Client {
    const val BASE_URL = "http://api.themoviedb.org/3/"
    var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}