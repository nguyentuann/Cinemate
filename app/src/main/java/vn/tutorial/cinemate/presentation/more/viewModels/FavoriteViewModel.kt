package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.usecase.favorite.AddFavoriteUseCase
import vn.tutorial.cinemate.domain.usecase.favorite.DeleteFavoriteUseCase
import vn.tutorial.cinemate.domain.usecase.favorite.GetFavoriteUseCase
import javax.inject.Inject

data class FavoriteUIState(
    val movies: List<MovieDetailModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
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
                getFavoriteUseCase(Unit)
            },
            onSuccess = { movies ->
                _state.value.copy(
                    isLoading = false,
                    error = null,
                    movies = movies ?: emptyList()
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

    fun addFavorite(movieId: String) {
        viewModelScope.launch {
            addFavoriteUseCase(movieId)
        }
        LogUtil("add with id $movieId")
    }

    fun deleteFavorite(movieId: String) {
        viewModelScope.launch {
            deleteFavoriteUseCase(movieId)
        }
        LogUtil("delete with id $movieId")
    }
}