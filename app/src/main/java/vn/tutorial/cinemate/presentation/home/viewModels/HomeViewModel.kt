package vn.tutorial.cinemate.presentation.home.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.CategoryModel
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.usecase.movies.GetBannerMoviesUseCase
import javax.inject.Inject

data class HomeUIState(
    val movies: List<MovieDetailModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBannerMoviesUseCase: GetBannerMoviesUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(HomeUIState())
    val state: StateFlow<HomeUIState> = _state

    init {
        refresh()
    }

    fun refresh() {
        getBannerMovies()
    }

    fun clearError() {
        _state.value = _state.value.copy(
            error = null
        )
    }

    fun getBannerMovies() {
        executeUseCase(
            state = _state,
            block = {
                getBannerMoviesUseCase(
                    GetBannerMoviesUseCase.Params(
                        page = 1,
                        size = 5,
                        sortBy = "year"
                    )
                )
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    error = null,
                    movies = it ?: emptyList()
                )
            },
            onError = {
                _state.value.copy(
                    isLoading = false,
                    error = it
                )
            },
            onLoading = {
                _state.value.copy(
                    isLoading = true,
                    error = null
                )
            }
        )
    }
}