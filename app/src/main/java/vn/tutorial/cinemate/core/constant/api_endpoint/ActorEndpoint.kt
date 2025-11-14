package vn.tutorial.cinemate.core.constant.api_endpoint

object ActorEndpoint {
    private const val PREFIX = "api/v1/actors/"

    const val GET_ALL_ACTORS = PREFIX

    const val GET_ACTOR_BY_ID = "${PREFIX}{actorId}"
}