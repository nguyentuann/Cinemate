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
    val qualities: List<String>? = null,
    val durationMinutes: Int? = null,
    val actors: List<ActorDirectorModel>? = null,
    val directors: List<ActorDirectorModel>? = null,
    val category: List<CategoryModel>? = null,
    val commentsCount: Int? = null,
    val filmUrl: String? = null,
    val watchDurationMinutes: Int? = null,
    val rank: Int? = null,
    val tags: List<String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class ActorDirectorModel(
    val id: String?,
    val fullName: String?
)
