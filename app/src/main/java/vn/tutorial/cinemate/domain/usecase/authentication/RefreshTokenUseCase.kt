package vn.tutorial.cinemate.domain.usecase.authentication

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<String, Resource<Unit?>>() {
    override suspend fun execute(param: String): Resource<Unit?> {
        return authRepository.refreshToken(
            refreshToken = param
        )
    }
}