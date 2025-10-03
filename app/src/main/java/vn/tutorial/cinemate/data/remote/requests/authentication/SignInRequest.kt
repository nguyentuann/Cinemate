package vn.tutorial.cinemate.data.remote.requests.authentication

data class SignInRequest(
    val email: String,
    val password: String
)