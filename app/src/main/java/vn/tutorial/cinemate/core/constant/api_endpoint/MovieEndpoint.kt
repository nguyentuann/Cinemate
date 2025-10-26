package vn.tutorial.cinemate.core.constant.api_endpoint

object MovieEndpoint {
    private const val PREFIX = "api/v1/movies/"

    const val GET_ALL_MOVIES = PREFIX

    const val GET_MOVIE_BY_ID = "${PREFIX}{movieId}"

    const val GET_MOVIES = PREFIX


    const val GET_REVIEWS_OF_MOVIE = "${PREFIX}{movieId}/reviews"

    const val CREATE_REVIEW = "${PREFIX}{movieId}/reviews"
    
    const val UPDATE_REVIEW = "${PREFIX}{movieId}/reviews/{reviewId}"

    const val DELETE_REVIEW = "${PREFIX}{movieId}/reviews/{reviewId}"

    const val GET_AVG_RATING = "${PREFIX}{movieId}/reviews/average-rating"

    const val GET_REVIEW_COUNT = "${PREFIX}{movieId}/reviews/count"

    const val GET_ACTORS_OF_MOVIE = "${PREFIX}{movieId}/actors"


}