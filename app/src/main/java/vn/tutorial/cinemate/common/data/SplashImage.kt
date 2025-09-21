package vn.tutorial.cinemate.common.data

class SplashImage(
    val image: Int,
    val description: String
)

val listSplashImage = listOf(
    SplashImage(
        image = vn.tutorial.cinemate.R.drawable.splash_1,
        description = "Discover new movies and TV shows"
    ),
    SplashImage(
        image = vn.tutorial.cinemate.R.drawable.splash_2,
        description = "Create your watchlist"
    ),
    SplashImage(
        image = vn.tutorial.cinemate.R.drawable.splash_3,
        description = "Get personalized recommendations"
    )
)