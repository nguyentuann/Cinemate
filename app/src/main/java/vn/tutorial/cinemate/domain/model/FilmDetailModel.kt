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

val filmMock = FilmDetailModel(
    id = "1",
    title = "The Joker",
    releaseDate = "1994-09-23",
    description = "\"In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. " +
            "He then embarks on a downward spiral of revolution and bloody crime. " +
            "This path brings him face-to-face with his alter-ego: the Joker.\",",
    verticalPoster = "https://www.vintagemovieposters.co.uk/wp-content/uploads/2020/01/IMG_2891.jpeg",
    horizontalPoster = "https://www.vintagemovieposters.co.uk/wp-content/uploads/2020/01/IMG_2891.jpeg",
    genres = listOf("Drama", "Crime"),
    trailerUrl = "https://tiktok-clone-taplamit.s3.ap-southeast-2.amazonaws.com/videos-hls/FF2_a_vyVj68aOyI4V1nZ/master.m3u8",
    age = 18,
    viewsCount = 2345678,
    rating = 4.7,
    year = 2019,
    country = "USA",
    quality = "HD",
    durationMinutes = 122,
    actors = listOf("Joaquin Phoenix", "Robert De Niro", "Zazie Beetz"),
    directors = listOf("Todd Phillips"),
    category = "Movie",
    commentsCount = 1234,
    filmUrl = "https://tiktok-clone-taplamit.s3.ap-southeast-2.amazonaws.com/videos-hls/FF2_a_vyVj68aOyI4V1nZ/master.m3u8",
    watchDurationMinutes = 120,
    rank = 1,
    tags = listOf("psychological", "thriller", "dc"),
    createdAt = "2019-10-01T12:00:00Z",
    updatedAt = "2020-01-01T12:00:00Z"

)
