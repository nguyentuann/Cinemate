package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
): BaseUseCase<VerifyEmailUseCase.Params, Resource<String?>>() {

    data class Params(
        val email: String
    )

    override suspend fun execute(param: Params): Resource<String?> {
        return authRepository.verifyEmail(
            email = param.email
        )
    }
}