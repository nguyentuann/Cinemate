package vn.tutorial.cinemate.mockdata

import vn.tutorial.cinemate.domain.model.Comment

val commentsData = listOf<Comment>(
    Comment(
        id = 1,
        userId = "user1",
        userName = "User1",
        stars = 4,
        content = "Great movie! Really enjoyed the plot and characters.",
        timestamp = "2 days ago"
    ),
    Comment(
        id = 2,
        userId = "user2",
        userName = "User2",
        stars = 4,
        content = "Amazing cinematography and soundtrack. A must-watch!",
        timestamp = "1 week ago"
    ),
    Comment(
        id = 3,
        userId = "user3",
        userName = "User3",
        stars = 2,
        content = "It was okay, but I felt the ending was a bit rushed.",
        timestamp = "3 days ago"
    ),
    Comment(
        id = 4,
        userId = "user4",
        userName = "User4",
        stars = 3,
        content = "Didn't live up to the hype. Found it quite boring.",
        timestamp = "5 days ago"
    ),
    Comment(
        id = 5,
        userId = "user5",
        userName = "User5",
        stars = 5,
        content = "Solid performances by the cast. Enjoyed it overall.",
        timestamp = "1 day ago"
    ),
)