package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<SignUpUseCase.Params, Resource<UserModel?>>() {
    data class Params(
        val email: String,
        val password: String,
        val token: String
    )

    override suspend fun execute(param: Params): Resource<UserModel?> {
        return authRepository.signUp(
            email = param.email,
            password = param.password,
            token = param.token
        )
    }
}
