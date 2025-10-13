package vn.tutorial.cinemate.data.remote.services

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.responses.authentication.BaseResponse

import com.google.gson.Gson
import retrofit2.Response
import vn.tutorial.cinemate.core.util.LogUtil

abstract class BaseService {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<BaseResponse<T>>
    ): Resource<T?> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.detail ?: body?.message ?: "Unexpected error")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                Resource.Error(
                    errorResponse?.detail ?: errorResponse?.message ?: "Unexpected error"
                )
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected error")
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
