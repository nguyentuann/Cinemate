package vn.tutorial.cinemate.mockdata

import vn.tutorial.cinemate.domain.model.MovieDetailModel

val sectionData = mapOf<String, List<MovieDetailModel>>(
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