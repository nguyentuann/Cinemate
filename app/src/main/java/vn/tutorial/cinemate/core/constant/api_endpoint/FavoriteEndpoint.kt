package vn.tutorial.cinemate.core.constant.api_endpoint

object FavoriteEndpoint {
    private const val PREFIX = "api/v1/favorites/"

    const val GET_FAVORITES_OF_USER = PREFIX

    const val ADD_FAVORITE = PREFIX

    const val DELETE_FAVORITE = "${PREFIX}{movieId}"
}