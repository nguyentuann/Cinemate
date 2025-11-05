package vn.tutorial.cinemate.core.constant.api_endpoint

object CategoryEndpoint {
    private const val PREFIX = "api/v1/categories/"

    const val GET_ALL_CATEGORIES = "${PREFIX}all"

    const val GET_MOVIES_BY_CATEGORY = "${PREFIX}{categoryId}/movies"


}