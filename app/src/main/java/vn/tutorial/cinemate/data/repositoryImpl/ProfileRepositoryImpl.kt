package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.responses.profile.toProfileModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.ProfileService
import vn.tutorial.cinemate.domain.model.ProfileModel
import vn.tutorial.cinemate.domain.model.toProfileRequest
import vn.tutorial.cinemate.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService
) : ProfileRepository, BaseService() {
    override suspend fun getProfile(): Resource<ProfileModel?> {
        return safeApiCall {
            profileService.getProfile()
        }.mapData {
            it?.toProfileModel()
        }
    }

    override suspend fun saveProfile(profile: ProfileModel): Resource<ProfileModel?> {
        return safeApiCall {
            profileService.updateProfile(profile.toProfileRequest())
        }.mapData {
            it?.toProfileModel()
        }
    }
}