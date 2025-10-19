package vn.tutorial.cinemate.domain.model

data class FilmDetailModel(
    val id: String,
    val title: String,
    val releaseDate: String,
    val description: String,
    val verticalPoster: String,
    val horizontalPoster: String,
    val genres: List<String>,
    val trailerUrl: String,
    val age: Int,
    val viewsCount: Int,
    val rating: Double,
    val year: Int,
    val country: String,
    val quality: String,
    val durationMinutes: Int,
    val actors: List<String>,
    val directors: List<String>,
    val category: String,
    val commentsCount: Int,
    val filmUrl: String? = null,
    val watchDurationMinutes: Int,
    val rank: Int,
    val tags: List<String>? = null,
    val createdAt: String,
    val updatedAt: String
)
