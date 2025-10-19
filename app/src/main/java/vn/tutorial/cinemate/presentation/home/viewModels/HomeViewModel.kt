package vn.tutorial.cinemate.presentation.home.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.domain.usecase.films.GetBannerFilmsUseCase
import vn.tutorial.cinemate.domain.usecase.films.GetSectionFilmsUseCase
import javax.inject.Inject

data class HomeUIState(
    val heroBannerFilms: List<FilmDetailModel> = emptyList(),
    val sectionFilms: Map<String, List<FilmDetailModel>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBannerFilmsUseCase: GetBannerFilmsUseCase,
    private val getSectionFilmsUseCase: GetSectionFilmsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUIState())
    val state = _state

    init {
        refresh()
        _state.value = _state.value.copy(isLoading = true)
    }

    fun refresh() {
        getBannerFilms()
        getSectionFilms()
    }

    fun clearError() {
        _state.value = _state.value.copy(
            error = null
        )
    }

    fun getBannerFilms() {
        executeUseCase(
            state = _state,
            block = {
                getBannerFilmsUseCase.invoke(Unit)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    error = null,
                    heroBannerFilms = it ?: emptyList()
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

    fun getSectionFilms() {
        executeUseCase(
            state = _state,
            block = {
                getSectionFilmsUseCase.invoke(Unit)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    error = null,
                    sectionFilms = it ?: emptyMap()
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