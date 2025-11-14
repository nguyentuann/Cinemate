package vn.tutorial.cinemate.common.data

import vn.tutorial.cinemate.R

class SplashImage(
    val image: Int,
    val description: String
)

val listSplashImage = listOf(
    SplashImage(
        image = R.drawable.splash_1,
        description = "Discover new movies and TV shows"
    ),
    SplashImage(
        image = R.drawable.splash_2,
        description = "Create your watchlist"
    ),
    SplashImage(
        image = R.drawable.splash_3,
        description = "Get personalized recommendations"
    )
)