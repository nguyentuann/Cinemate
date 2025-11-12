package vn.tutorial.cinemate.domain.model

data class MovieDetailModel(
    val id: String,
    val title: String,
    val releaseDate: String? = null,
    val description: String? = null,
    val verticalPoster: String? = null,
    val horizontalPoster: String?=null,
    val genres: List<String>? = null,
    val trailerUrl: String? = null,
    val age: Int? = null,
    val viewsCount: Int? = null,
    val rating: Double? = null,
    val year: Int? = null,
    val country: String? = null,
    val qualities: Map<String, String>? = null,
    val durationMinutes: Int? = null,
    val actors: List<String>? = null,
    val directors: List<String>? = null,
    val category: String? = null,
    val commentsCount: Int? = null,
    val filmUrl: String? = null,
    val watchDurationMinutes: Int? = null,
    val rank: Int? = null,
    val tags: List<String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
