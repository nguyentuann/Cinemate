package vn.tutorial.cinemate.domain.usecase.profile

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.ProfileRepository
import java.io.File
import javax.inject.Inject

class UpdateAvatarUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
): BaseUseCase<File, Resource<String?>>() {
    override suspend fun execute(param: File): Resource<String?> {
       return profileRepository.updateAvatar(param)
    }
}