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
                "Once Upon a Time... in Holliwood",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1599028/70580cf5-3287-42d6-8a76-2c715e2f6172/600x900"
            )
        )

        list.add(
            Movie(
                "The Wolf of The Wall Street",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/5c758ac0-7a5c-4f00-a94f-1be680a312fb/600x900"
            )
        )

        list.add(
            Movie(
                "Interception",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/be2abfcb-c664-43f3-b82e-8af1ac75c2a4/600x900"
            )
        )

        list.add(
            Movie(
                "The Godfather",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1599028/c11652e8-653b-47c1-8e72-1552399a775b/600x900"
            )
        )

        list.add(
            Movie(
                "Die Hard",
                1999,
                9.0,
                "https://avatars.mds.yandex.net/get-kinopoisk-image/1704946/f55f782b-2dcd-4e4c-be87-0cc13333f857/600x900"
            )
        )


        return list
    }
}