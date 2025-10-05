package vn.tutorial.cinemate.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import vn.tutorial.cinemate.data.local.LocalStorage
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val localStorage: LocalStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        localStorage.getAccessToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
