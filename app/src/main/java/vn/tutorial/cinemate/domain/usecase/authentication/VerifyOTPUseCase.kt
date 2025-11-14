package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyOTPUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<VerifyOTPUseCase.Params, Resource<Boolean?>>() {

    data class Params(
        val email: String,
        val otp: String
    )

    override suspend fun execute(param: Params): Resource<Boolean?> {
        return authRepository.verifyOTP(
            email = param.email,
            otp = param.otp
        )
    }
}