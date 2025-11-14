package vn.tutorial.cinemate.core.constant.api_endpoint

object PlayMovieEndpoint {
    private const val PREFIX = BaseEndpoint.BASE_URL

    const val PLAY_MOVIE = "${PREFIX}streams/movies/"

    const val PLAY_TRAILER = BaseEndpoint.AVT_URL
}