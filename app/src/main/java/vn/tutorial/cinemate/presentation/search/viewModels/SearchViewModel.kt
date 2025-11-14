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
import kotlin.collections.plus

data class SearchUiState(
    val query: String = "",
    val movieResults: List<MovieDetailModel>? = null,
    val movieByCategoryResults: List<MovieDetailModel>? = null,
    val categoryId: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val hasMore: Boolean = true,
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
        _state.value = _state.value.copy(movieResults = emptyList(), query = "")
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun updateCategory(categoryId: String) {
        _state.value = _state.value.copy(
            categoryId = categoryId,
            movieResults = emptyList(),
            movieByCategoryResults = emptyList(),
            page = 1,
            hasMore = true
        )
    }


    fun startNewSearch() {
        _state.value = _state.value.copy(
            movieResults = emptyList(),
            movieByCategoryResults = emptyList(),
            page = 1,
            hasMore = true
        )
    }



    fun getMoviesByCategory() {
        executeUseCase(
            state = _state,
            block = {
                getMovieByCategoryUseCase(_state.value.categoryId)
            },
            onSuccess = {
                _state.value.copy(
                    movieByCategoryResults = it ?: emptyList(),
                    movieResults = emptyList(),
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

    fun search() {

        LogUtil("call search")
        if (state.value.query.isBlank()) {
            return
        }

        executeUseCase(
            state = _state,
            block = {
                searchUseCase(
                    SearchUseCase.Params(
                        query = _state.value.query,
                        page = _state.value.page,
                        size = 5,
                    )
                )
            },
            onSuccess = {
                val currentMovies = _state.value.movieResults.orEmpty()
                val newMovies = it.orEmpty()
                val updatedMovies = currentMovies + newMovies
                val hasMore = (it?.size ?: 0) >= 5

                _state.value.copy(
                    movieResults = updatedMovies,
                    movieByCategoryResults = emptyList(),
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

}