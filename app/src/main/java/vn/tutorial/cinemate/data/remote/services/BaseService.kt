package vn.tutorial.cinemate.data.remote.services

import com.google.gson.Gson
import retrofit2.Response
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.remote.responses.BaseResponse

abstract class BaseService {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<BaseResponse<T>>
    ): Resource<T?> {
        return try {
            var response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    LogUtil("vao success co body: $body")
                    Resource.Success(body.data)
                } else {
                    LogUtil("vao success ko body: $body")
                    Resource.Error(body?.detail ?: body?.message ?: "Unexpected error")
                }
            }
//            else if (response.code() == 401) {
//                // token hết hạn, thử refresh
//                val refreshed = refreshToken()
//                if (refreshed) {
//
//                    // retry lại apiCall
//                    response = apiCall()
//                    if (response.isSuccessful) {
//                        val body = response.body()
//                        if (body != null && body.status == "success") {
//                            Resource.Success(body.data)
//                        } else {
//                            Resource.Error(body?.detail ?: body?.message ?: "Unexpected error")
//                        }
//                    } else {
//                        val errorBody = response.errorBody()?.string()
//                        val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
//                        Resource.Error(
//                            errorResponse?.detail ?: errorResponse?.message ?: "Unexpected error"
//                        )
//                    }
//                } else {
//                    Resource.Error("Session expired. Please login again.")
//                }
//            }
            else {
                val errorBody = response.errorBody()?.string()
                LogUtil("vao error co body: $errorBody")
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                Resource.Error(
                    errorResponse?.detail ?: errorResponse?.message ?: "Unexpected error"
                )
            }
        } catch (e: Exception) {
            LogUtil("vao catch: ${e.message}")
            Resource.Error(e.message ?: "Please try again later")
        }
    }

    protected fun <T, R> Resource<T>.mapData(transform: (T?) -> R?): Resource<R?> {
        return when (this) {
            is Resource.Success -> Resource.Success(transform(data))
            is Resource.Error -> Resource.Error(message)
            is Resource.Loading -> Resource.Loading
        }
    }
}
