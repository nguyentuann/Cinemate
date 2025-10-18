package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<ChangePasswordUseCase.Params, Resource<String?>>() {

    data class Params(
        val oldPassword: String,
        val newPassword: String,
        val confirmPassword: String
    )

    override suspend fun execute(param: Params): Resource<String?> {
        return authRepository.changePassword(
            oldPassword = param.oldPassword,
            newPassword = param.newPassword,
            confirmPassword = param.confirmPassword
        )
    }
}