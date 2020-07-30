package ru.dmitry.moviesearch

import ru.dmitry.moviesearch.model.Movie

object DataSource {
    fun createDataSet(): ArrayList<Movie>{
        val list = ArrayList<Movie>()
        list.add(
            Movie(
                "Friends",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/35c6cb89-75e3-4efa-8d00-5bbf7175c066/300x450"
            )
        )

        list.add(
            Movie(
                "Friends",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/35c6cb89-75e3-4efa-8d00-5bbf7175c066/300x450"
            )
        )

        list.add(
            Movie(
                "Friends",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/35c6cb89-75e3-4efa-8d00-5bbf7175c066/300x450"
            )
        )

        list.add(
            Movie(
                "Friends",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/35c6cb89-75e3-4efa-8d00-5bbf7175c066/300x450"
            )
        )

        list.add(
            Movie(
                "Friends",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/35c6cb89-75e3-4efa-8d00-5bbf7175c066/300x450"
            )
        )

        list.add(
            Movie(
                "Friends",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/35c6cb89-75e3-4efa-8d00-5bbf7175c066/300x450"
            )
        )


        return list
    }
}