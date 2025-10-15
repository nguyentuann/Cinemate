package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<String, Resource<String?>>() {
    override suspend fun execute(param: String): Resource<String?> {
        return authRepository.verifyToken(param)
    }
}