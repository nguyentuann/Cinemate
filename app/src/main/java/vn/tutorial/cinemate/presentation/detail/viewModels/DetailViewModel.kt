package vn.tutorial.cinemate.presentation.detail.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.domain.usecase.films.GetDetailFilmUseCase
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val filmDetail: FilmDetailModel? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailFilmUseCase: GetDetailFilmUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state

    fun clearError() {
        _state.value = _state.value.copy(
            errorMessage = null
        )
    }

    fun getDetailFilm(filmId: String) {
        executeUseCase(
            state = _state,
            block = {
                getDetailFilmUseCase(filmId)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    filmDetail = it
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