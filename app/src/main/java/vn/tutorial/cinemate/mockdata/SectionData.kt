package vn.tutorial.cinemate.mockdata

import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.domain.model.filmMock

val sectionData = mapOf<String, List<FilmDetailModel>>(
    "Popular on Cinemate" to listOf(
        filmMock,
        filmMock,
        filmMock
    ),

    "American Movies" to listOf(
        filmMock,
        filmMock,
        filmMock,
        filmMock,
    ),

    "New Release" to listOf(
        filmMock,
        filmMock,
        filmMock,
    )
)