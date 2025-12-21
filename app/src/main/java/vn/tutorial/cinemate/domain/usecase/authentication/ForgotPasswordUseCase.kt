package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<VerifyEmailUseCase.Params, Resource<String?>>() {
    override suspend fun execute(param: VerifyEmailUseCase.Params): Resource<String?> {
        return authRepository.forgotPassword(
            email = param.email,
        )
    }
}