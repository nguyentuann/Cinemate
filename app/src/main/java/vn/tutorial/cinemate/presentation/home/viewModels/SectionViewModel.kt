package vn.tutorial.cinemate.presentation.home.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.usecase.movies.GetSectionMoviesUseCase
import vn.tutorial.cinemate.domain.usecase.movies.GetTop10MoviesUseCase
import javax.inject.Inject

data class SectionUIState(
    val movies: List<MovieDetailModel> = emptyList(),
    val page: Int = 1,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class Top10UIState(
    val movies: List<MovieDetailModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class SectionType {
    NEW, RECOMMENDED
}

@HiltViewModel
class SectionViewModel @Inject constructor(
    private val getSectionMoviesUseCase: GetSectionMoviesUseCase,
    private val getTop10Movies: GetTop10MoviesUseCase
) : ViewModel() {
    private val _sectionsState = MutableStateFlow(
        mapOf(
            SectionType.NEW to SectionUIState(),
            SectionType.RECOMMENDED to SectionUIState()
        )
    )

    val sectionsState: StateFlow<Map<SectionType, SectionUIState>> = _sectionsState

    private val _top10State = MutableStateFlow(Top10UIState())
    val top10State: StateFlow<Top10UIState> = _top10State

    init {
        getSectionMovies(SectionType.NEW, "releaseDate")
        getSectionMovies(SectionType.RECOMMENDED, "year")
        getTop10Movies()
    }

    fun getSectionMovies(section: SectionType, sortBy: String) {
        val currentState = _sectionsState.value[section] ?: return
        executeUseCase(
            state = MutableStateFlow(currentState),
            block = {
                getSectionMoviesUseCase(
                    GetSectionMoviesUseCase.Params(
                        section = section.name.lowercase(),
                        page = currentState.page,
                        size = 5,
                        sortBy = sortBy,
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

    fun getTop10Movies() {
        executeUseCase(
            state = _top10State,
            block = {
                getTop10Movies.invoke(Unit)
            },
            onSuccess = {
               _top10State.value.copy(
                    isLoading = false,
                    error = null,
                    movies = it ?: emptyList()
                )
            },
            onError = {
                top10State.value.copy(
                    isLoading = false,
                    error = it
                )
            },
            onLoading = {
                top10State.value.copy(
                    isLoading = true,
                    error = null
                )
            }
        )
    }
}