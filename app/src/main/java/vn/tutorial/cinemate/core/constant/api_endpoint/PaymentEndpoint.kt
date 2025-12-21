package vn.tutorial.cinemate.core.constant.api_endpoint

object PaymentEndpoint {
    private const val PREFIX = "subscription-plans"

    const val GET_ALL = PREFIX

    const val GET_BY_ID = "${PREFIX}/{planId}"

    const val CREATE_SUBSCRIPTION = "subscriptions"

    const val GET_SUBSCRIPTION = "subscriptions/current"

    const val GET_MEMBERS = "family-plans/members"

    const val REMOVE_MEMBER = "family-plans/members/{memberUserId}"

    const val INVITE_MEMBER = "family-plans/invitations"

    const val ACCEPT_INVITATION = "family-plans/invitations/accept"


    const val SET_CHILDREN_MODE = "family-plans/parent-control"


    const val  SEARCH_EMAIL = "users/search"

    const val CANCEL_PLAN = "subscriptions/{subscriptionId}/cancel"


    const val GET_CHILDREN_MODE = "family-plans/parent-control"
}