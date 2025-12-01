package vn.tutorial.cinemate.presentation.home.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.usecase.movies.GetSectionMoviesUseCase
import javax.inject.Inject

data class SectionUIState(
    val movies: List<MovieDetailModel> = emptyList(),
    val page: Int = 1,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class SectionType {
    NEW, TRENDING, RECOMMENDED
}

@HiltViewModel
class SectionViewModel @Inject constructor(
    private val getSectionMoviesUseCase: GetSectionMoviesUseCase
) : ViewModel() {
    private val _sectionsState = MutableStateFlow(
        mapOf(
            SectionType.NEW to SectionUIState(),
            SectionType.TRENDING to SectionUIState(),
            SectionType.RECOMMENDED to SectionUIState()
        )
    )

    val sectionsState: StateFlow<Map<SectionType, SectionUIState>> = _sectionsState

    init {
        getSectionMovies(SectionType.NEW, "releaseDate")
        getSectionMovies(SectionType.TRENDING, "rank")
        getSectionMovies(SectionType.RECOMMENDED, "year")
    }

    fun getSectionMovies(section: SectionType, sortBy: String) {
        val currentState = _sectionsState.value[section] ?: return
        executeUseCase(
            state = MutableStateFlow(currentState),
            block = {
                val sortDirection = if (section == SectionType.TRENDING) "asc" else "desc"
                getSectionMoviesUseCase(
                    GetSectionMoviesUseCase.Params(
                        section = section.name.lowercase(),
                        page = currentState.page,
                        size = 5,
                        sortBy = sortBy,
                        sortDirection = sortDirection
                    )
                )
            },

            onSuccess = { newMovies ->
                val updatedMovies = currentState.movies + (newMovies ?: emptyList())
                val hasMore = (newMovies?.size ?: 0) >= 5
                _sectionsState.value = _sectionsState.value.toMutableMap().apply {
                    this[section] = currentState.copy(
                        movies = updatedMovies,
                        page = currentState.page + 1,
                        hasMore = hasMore,
                        isLoading = false,
                        error = null
                    )
                }
            },
            onError = { errorMsg ->
                _sectionsState.value = _sectionsState.value.toMutableMap().apply {
                    this[section] = currentState.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                }
            },
            onLoading = {
                _sectionsState.value = _sectionsState.value.toMutableMap().apply {
                    this[section] = currentState.copy(
                        isLoading = true,
                        error = null
                    )
                }
            }
        )
    }
}