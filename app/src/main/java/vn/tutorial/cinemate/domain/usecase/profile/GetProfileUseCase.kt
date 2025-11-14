package vn.tutorial.cinemate.domain.usecase.profile

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ProfileModel
import vn.tutorial.cinemate.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
): BaseUseCase<Unit, Resource<ProfileModel?>>() {
    override suspend fun execute(param: Unit): Resource<ProfileModel?> {
        return profileRepository.getProfile()
    }
}