package vn.tutorial.cinemate.presentation.home.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import javax.inject.Inject


data class SectionUIState(
    val sectionTitle: String = "",
    val films: List<FilmDetailModel> = emptyList(),
    val page: Int = 1,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SectionViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(SectionUIState())
    val state: StateFlow<SectionUIState> = _state


    fun clearError() {
        _state.value = _state.value.copy(
            error = null
        )
    }


    fun getSectionFilms() {
        // todo call to get section films
    }


}