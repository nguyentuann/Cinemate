package vn.tutorial.cinemate.presentation.detail.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.domain.usecase.reviews.AddReviewUseCase
import vn.tutorial.cinemate.domain.usecase.reviews.GetAllReviewsUseCase
import vn.tutorial.cinemate.domain.usecase.reviews.GetReviewCountUseCase
import javax.inject.Inject

data class ReviewUIState(
    var comments: List<ReviewModel> = emptyList(),
    var commentCount: Int = 0,
    val error: String? = null,
    val loading: Boolean = false
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getAllReviewsUseCase: GetAllReviewsUseCase,
    private val addReviewUseCase: AddReviewUseCase,
    private val getReviewCountUseCase: GetReviewCountUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ReviewUIState())
    val state: StateFlow<ReviewUIState> = _state


    fun clearError() {
        _state.value = _state.value.copy(
            error = null,
        )
    }

    fun  getReviewCount(movieId: String) {
        executeUseCase(
            state = _state,
            block = {
                getReviewCountUseCase(movieId)
            },
            onSuccess = {
                _state.value.copy(
                    commentCount = it ?: 0,
                    error = null,
                    loading = false
                )
            },
            onError = {
                _state.value.copy(
                    commentCount = 0,
                    error = it,
                    loading = false
                )
            },
            onLoading = {
                state.value.copy(
                    loading = true,
                    error = null
                )
            }
        )
    }

    fun getAllReviews(movieId: String) {
        executeUseCase(
            state = _state,
            block = {
                getAllReviewsUseCase(movieId)
            },
            onSuccess = {
                _state.value.copy(
                    comments = it ?: emptyList(),
                    error = null,
                    loading = false
                )
            },
            onError = {
                _state.value.copy(
                    comments = emptyList(),
                    error = it,
                    loading = false
                )
            },
            onLoading = {
                state.value.copy(
                    loading = true,
                    error = null
                )
            }
        )
    }

    fun addReview(reviewModel: ReviewModel) {
        executeUseCase(
            state = _state,
            block = {
                addReviewUseCase(reviewModel)
            },
            onSuccess = {
                _state.value.copy(
                    comments = _state.value.comments + it!!,
                    error = null,
                    loading = false
                )
            },
            onError = {
                _state.value.copy(
                    error = it,
                    loading = false
                )
            },
            onLoading = {
                state.value.copy(
                    loading = true,
                    error = null
                )
            }
        )
    }

    fun updateComment(filmId: String, comment: ReviewModel) {

    }

    fun deleteComment(filmId: String, commentId: Int) {

    }
}