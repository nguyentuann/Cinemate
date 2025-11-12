package vn.tutorial.cinemate.data.repositoryImpl

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.constant.api_endpoint.BaseEndpoint
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.local.LocalStorage
import vn.tutorial.cinemate.data.remote.responses.profile.toProfileModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.ProfileService
import vn.tutorial.cinemate.domain.model.ProfileModel
import vn.tutorial.cinemate.domain.model.toProfileRequest
import vn.tutorial.cinemate.domain.repository.ProfileRepository
import java.io.File
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
    private val localStorage: LocalStorage
) : ProfileRepository, BaseService() {

    override suspend fun getProfile(): Resource<ProfileModel?> {
        return safeApiCall {
            profileService.getProfile()
        }.mapData {
            val userName = it?.firstName
            val userAvatar = it?.avatarUrl

            val newURL = userAvatar?.replace("http://localhost:9000/", BaseEndpoint.BASE_URL)


            localStorage.saveUserName(userName ?: "Anonymous User")
            localStorage.saveUserAvatar(newURL ?: "")

            it?.toProfileModel()
        }
    }

    override suspend fun saveProfile(profile: ProfileModel): Resource<ProfileModel?> {
        return safeApiCall {
            profileService.updateProfile(profile.toProfileRequest())
        }.mapData {
            val userName = it?.firstName
            val userAvatar = it?.avatarUrl

            val newURL = userAvatar?.replace("http://localhost:9000/", BaseEndpoint.BASE_URL)

            localStorage.saveUserName(userName ?: "Anonymous User")
            localStorage.saveUserAvatar(newURL ?: "")

            it?.toProfileModel()
        }
    }

    override suspend fun updateAvatar(file: File): Resource<String?> {
        LogUtil("goi update avatar repo impl")
        return safeApiCall {
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            profileService.updateAvatar(body)
        }.mapData {
            val newURL = it?.imageUrl?.replace("http://localhost:9000/", BaseEndpoint.BASE_URL)
            localStorage.saveUserAvatar(newURL ?: "")
            newURL
        }
    }
}