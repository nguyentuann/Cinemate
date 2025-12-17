package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.usecase.favorite.AddFavoriteUseCase
import vn.tutorial.cinemate.domain.usecase.favorite.DeleteFavoriteUseCase
import vn.tutorial.cinemate.domain.usecase.favorite.GetFavoriteUseCase
import javax.inject.Inject
import kotlin.collections.orEmpty
import kotlin.collections.plus

data class FavoriteUIState(
    val movies: List<MovieDetailModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val hasMore: Boolean = true,
)

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(FavoriteUIState())
    val state: StateFlow<FavoriteUIState> = _state

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun getFavoriteMovies() {
        executeUseCase(
            state = _state,
            block = {
                getFavoriteUseCase(
                    GetFavoriteUseCase.Params(
                        page = _state.value.page,
                        size = 5,
                    )
                )
            },
            onSuccess = {
                val currentMovies = _state.value.movies.orEmpty()
                val newMovies = it.orEmpty()
                val updatedMovies = currentMovies + newMovies
                val hasMore = (it?.size ?: 0) >= 5

                _state.value.copy(
                    movies = updatedMovies,
                    isLoading = false,
                    error = null,
                    page = _state.value.page + 1,
                    hasMore = hasMore
                )
            },
            onError = { errorMsg ->
                _state.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
            },
            onLoading = {
                _state.value.copy(isLoading = true, error = null)
            }
        )
    }

    fun addFavorite(movieId: String, onSuccess: () -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                addFavoriteUseCase(movieId)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    error = null,
                ).also {
                    onSuccess()
                }
            },
            onError = {
                _state.value.copy(
                    isLoading = false,
                    error = it,
                )
            },
            onLoading = {
                _state.value.copy(
                    isLoading = true,
                    error = null,
                )
            }
        )
    }

    fun deleteFavorite(movieId: String) {
        executeUseCase(
            state = _state,
            block = {
                deleteFavoriteUseCase(movieId)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    error = null,
                )
            },
            onError = {
                _state.value.copy(
                    isLoading = false,
                    error = it,
                )
            },
            onLoading = {
                _state.value.copy(
                    isLoading = true,
                    error = null,
                )
            }
        )
    }
}