package vn.tutorial.cinemate.presentation.search.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.CategoryModel
import vn.tutorial.cinemate.domain.usecase.search.GetCategoriesUseCase
import javax.inject.Inject

data class CategoryUiState(
    val categories: List<CategoryModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private var _state = MutableStateFlow(CategoryUiState())
    val state: StateFlow<CategoryUiState> = _state

    init {
        getCategories()
    }

    fun getCategories() {
        executeUseCase(
            state = _state,
            block = {
                getCategoriesUseCase(Unit)
            },
            onSuccess = {
                _state.value.copy(
                    categories = it ?: emptyList(),
                    isLoading = false,
                    error = null
                )
            },
            onError = { errorMsg ->
                LogUtil("Get categories error: $errorMsg")
                _state.value.copy(
                    isLoading = false,
                    error = errorMsg
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