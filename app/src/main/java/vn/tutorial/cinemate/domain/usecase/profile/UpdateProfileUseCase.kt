package vn.tutorial.cinemate.domain.usecase.profile

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ProfileModel
import vn.tutorial.cinemate.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseUseCase<
        ProfileModel, Resource<ProfileModel?>>() {

    override suspend fun execute(param: ProfileModel): Resource<ProfileModel?> {
        return profileRepository.saveProfile(param)
    }
}