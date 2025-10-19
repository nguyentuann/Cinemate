package vn.tutorial.cinemate.mockdata

import vn.tutorial.cinemate.domain.model.FilmDetailModel

val sectionData = mapOf<String, List<FilmDetailModel>>(
    "Popular on Cinemate" to listOf(
        filmMock1,
        filmMock2,
        filmMock3
    ),

    "American Movies" to listOf(
        filmMock1,
        filmMock2,
        filmMock3,
        filmMock4,
    ),

    "New Release" to listOf(
        filmMock2,
        filmMock3,
        filmMock4,
    )
)