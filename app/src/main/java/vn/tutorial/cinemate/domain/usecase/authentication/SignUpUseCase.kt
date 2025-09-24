package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.domain.usecase.BaseUseCase
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
//    private val authRepository: AuthRepository
) : BaseUseCase<SignUpUseCase.Params, Unit>() {
    data class Params(val email: String, val password: String)

    override suspend fun execute(param: Params): Unit {
    }
}
