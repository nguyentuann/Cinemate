package vn.tutorial.cinemate.core.helper

import vn.tutorial.cinemate.core.constant.api_endpoint.BaseEndpoint

fun getFullAvatarUrl (path: String?): String {
    if (path.isNullOrEmpty()) {
        return ""
    }
    return "${BaseEndpoint.AVT_URL}${path}"
}