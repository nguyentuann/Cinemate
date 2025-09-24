package vn.tutorial.cinemate.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseUseCase<in Params, out Result>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(param: Params): Result =
        withContext(dispatcher) {
            execute(param)
        }

    protected abstract suspend fun execute(param: Params): Result

}