package vn.tutorial.cinemate.mockdata

import vn.tutorial.cinemate.domain.model.ReviewModel

val commentsData = listOf<ReviewModel>(
    ReviewModel(
        id = "1",
        customerId = "user1",
        movieId = "movie1",
        userName = "User1",
        userAvatar = "",
        stars = 4,
        content = "Great movie! Really enjoyed the plot and characters.",
        createAt = "2025-11-03T16:45:17.133539Z",
        updateAt = "2025-11-03T16:45:17.133539Z"
    ),
    ReviewModel(
        id = "2",
        customerId = "user2",
        movieId = "movie1",
        userName = "User2",
        stars = 4,
        userAvatar = "",
        content = "Amazing cinematography and soundtrack. A must-watch!",
        createAt = "2025-11-03T16:45:17.133539Z",
        updateAt = "2025-11-03T16:45:17.133539Z",
    ),
    ReviewModel(
        id = "3",
        customerId = "user4",
        movieId = "movie1",
        userName = "User3",
        stars = 4,
        userAvatar = "",
        content = "Amazing cinematography and soundtrack. A must-watch!",
        createAt = "2025-11-03T16:45:17.133539Z",
        updateAt = "2025-11-03T16:45:17.133539Z",
    ),
    ReviewModel(
        id = "4",
        customerId = "user4",
        movieId = "movie1",
        userName = "User4",
        stars = 4,
        userAvatar = "",
        content = "Amazing cinematography and soundtrack. A must-watch!",
        createAt = "2025-11-03T16:45:17.133539Z",
        updateAt = "2025-11-03T16:45:17.133539Z",
    ),
)