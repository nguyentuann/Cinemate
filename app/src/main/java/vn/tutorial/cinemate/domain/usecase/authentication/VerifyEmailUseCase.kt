package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.domain.usecase.BaseUseCase
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    // private val authRepository: AuthRepository
) : BaseUseCase<String, Unit>() {
    override suspend fun execute(param: String) {

    }
}