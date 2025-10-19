package vn.tutorial.cinemate.domain.model

data class HomeFilmsModel(
    val title: String,
    val films: List<FilmDetailModel>
)