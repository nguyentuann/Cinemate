package vn.tutorial.cinemate.presentation.detail.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.usecase.movies.GetDetailMovieUseCase
import vn.tutorial.cinemate.domain.usecase.movies.GetSectionMoviesUseCase
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val movieDetail: MovieDetailModel? = null,
    val recommendMovies: List<MovieDetailModel> = emptyList(),
    val page: Int = 1,
    val hasMore: Boolean = true,
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailMovieUseCase: GetDetailMovieUseCase,
    private val getSectionMoviesUseCase: GetSectionMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state

    fun clearError() {
        _state.value = _state.value.copy(
            errorMessage = null
        )
    }

    fun getDetailMovie(movieId: String, onSuccess: () -> Unit = {}) {
        executeUseCase(
            state = _state,
            block = {
                getDetailMovieUseCase(movieId)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    movieDetail = it
                ).also {
                    onSuccess()
                }
            },
            onError = {
                _state.value.copy(
                    isLoading = false,
                    errorMessage = it,
                )
            },
            onLoading = {
                state.value.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
        )
    }

    fun getRecommendMovies() {
        executeUseCase(
            state = _state,
            block = {
                getSectionMoviesUseCase(
                    GetSectionMoviesUseCase.Params(
                        section = "new",
                        page = _state.value.page,
                        size = 5,
                        sortBy = "year"
                    )
                )
            },
            onSuccess = {
                val updatedMovies = _state.value.recommendMovies + (it ?: emptyList())
                val hasMore = (it?.size ?: 0) >= 5
                _state.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    recommendMovies =  updatedMovies,
                    page = _state.value.page + 1,
                    hasMore = hasMore
                )
            },
            onError = {
                _state.value.copy(
                    isLoading = false,
                    errorMessage = it,
                )
            },
            onLoading = {
                state.value.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
        )
    }
}