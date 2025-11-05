package vn.tutorial.cinemate.presentation.search.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.usecase.search.GetMovieByCategoryUseCase
import vn.tutorial.cinemate.domain.usecase.search.SearchUseCase
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val filmResults: List<MovieDetailModel> = emptyList(),
    val category: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val getMovieByCategoryUseCase: GetMovieByCategoryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state

    fun updateQuery(query: String) {
        _state.value = _state.value.copy(query = query)
    }

    fun clearResults() {
        _state.value = _state.value.copy(filmResults = emptyList(), query = "")
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun updateCategory(category: String) {
        _state.value = _state.value.copy(category = category)
    }

    fun search() {
        LogUtil("Call search with query: ${_state.value.query} va category: ${_state.value.category}")
        executeUseCase(
            state = _state,
            block = {
                searchUseCase(
                    SearchUseCase.Params(
                        query = _state.value.query
                    )
                )
            },
            onSuccess = {
                _state.value.copy(
                    filmResults = it ?: emptyList(),
                    isLoading = false,
                    error = null
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

}