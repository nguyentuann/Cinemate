package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<ResetPasswordUseCase.Params, Resource<String?>>() {

    data class Params(
        val email: String,
        val otp: String,
        val newPassword: String,
    )

    override suspend fun execute(param: ResetPasswordUseCase.Params): Resource<String?> {
        return authRepository.forgotPassword(
            email = param.email,
            otp = param.otp,
            newPassword = param.newPassword
        )
    }
}