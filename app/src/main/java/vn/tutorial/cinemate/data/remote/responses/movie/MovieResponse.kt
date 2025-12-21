package vn.tutorial.cinemate.data.remote.responses.movie

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.core.constant.api_endpoint.BaseEndpoint
import vn.tutorial.cinemate.core.helper.getFullAvatarUrl
import vn.tutorial.cinemate.domain.model.ActorDirectorModel
import vn.tutorial.cinemate.domain.model.CategoryModel
import vn.tutorial.cinemate.domain.model.MovieDetailModel

data class MovieResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("qualities")
    val qualities: List<String>? = null,
    @SerializedName("verticalPoster")
    val verticalPoster: String?=null,
    @SerializedName("horizontalPoster")
    val horizontalPoster: String?=null,
    @SerializedName("releaseDate")
    val releaseDate: String? = null,
    @SerializedName("trailerUrl")
    val trailerUrl: String? = null,
    @SerializedName("age")
    val age: Int? = null,
    @SerializedName("year")
    val year: Int? = null,
    @SerializedName("country")
    val country: String? = null,
    // additional fields
    @SerializedName("genres")
    val genres: List<String>? = null,
    @SerializedName("viewsCount")
    val viewsCount: Int? = null,
    @SerializedName("rating")
    val rating: Double? = null,
    @SerializedName("durationMinutes")
    val durationMinutes: Int? = null,
    @SerializedName("actors")
    val actors: List<ActorDirector>? = null,
    @SerializedName("directors")
    val directors: List<ActorDirector>? = null,
    @SerializedName("categories")
    val category: List<CategoryResponse>? = null,
    @SerializedName("commentsCount")
    val commentsCount: Int? = null,
    @SerializedName("filmUrl")
    val filmUrl: String? = null,
    @SerializedName("watchDurationMinutes")
    val watchDurationMinutes: Int? = null,
    @SerializedName("rank")
    val rank: Int? = null,
    @SerializedName("tags")
    val tags: List<String>? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)


data class ActorDirector(
    @SerializedName("id")
    val id: String?,
    @SerializedName("fullName")
    val fullName: String?,
)

fun ActorDirector.toActorDirectorModel(): ActorDirectorModel {
    return ActorDirectorModel(
        id = id,
        fullName = fullName
    )
}



// Extension to map response -> domain model
fun MovieResponse.toMovieDetailModel(): MovieDetailModel {
    return MovieDetailModel(
        id = id,
        title = title,
        releaseDate = releaseDate,
        description = description,
        verticalPoster = getFullAvatarUrl(verticalPoster),
        horizontalPoster = getFullAvatarUrl(horizontalPoster),
        genres = genres,
        trailerUrl = trailerUrl,
        age = age,
        viewsCount = viewsCount,
        rating = rating,
        year = year,
        country = country,
        qualities = qualities,
        durationMinutes = durationMinutes,
        actors = actors?.map { it.toActorDirectorModel() },
        directors = directors?.map { it.toActorDirectorModel() },
        category = category?.map { it.toCategoryModel() },
        commentsCount = commentsCount,
        filmUrl = filmUrl,
        watchDurationMinutes = watchDurationMinutes,
        rank = rank,
        tags = tags,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}