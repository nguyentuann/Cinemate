package vn.tutorial.cinemate.presentation.detail.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.domain.usecase.reviews.AddReviewUseCase
import vn.tutorial.cinemate.domain.usecase.reviews.DeleteReviewUseCase
import vn.tutorial.cinemate.domain.usecase.reviews.GetAllReviewsUseCase
import vn.tutorial.cinemate.domain.usecase.reviews.GetReviewCountUseCase
import javax.inject.Inject

data class ReviewUIState(
    var reviews: List<ReviewModel> = emptyList(),
    var reviewCount: Int = 0,
    val error: String? = null,
    val loading: Boolean = false
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getAllReviewsUseCase: GetAllReviewsUseCase,
    private val addReviewUseCase: AddReviewUseCase,
    private val getReviewCountUseCase: GetReviewCountUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase
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
                    reviewCount = it ?: 0,
                    error = null,
                    loading = false
                )
            },
            onError = {
                _state.value.copy(
                    reviewCount = 0,
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
                    reviews = it ?: emptyList(),
                    error = null,
                    loading = false
                )
            },
            onError = {
                _state.value.copy(
                    reviews = emptyList(),
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
                    reviews = _state.value.reviews + it!!,
                    reviewCount = _state.value.reviewCount + 1,
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


    fun deleteComment(movieId: String, reviewId: String, customerId: String) {
        executeUseCase(
            state = _state,
            block = {
                deleteReviewUseCase(
                    DeleteReviewUseCase.Params(
                        reviewId = reviewId,
                        movieId = movieId,
                        userId = customerId
                    )
                )
            },
            onSuccess = {
                _state.value.copy(
                    reviews = _state.value.reviews.filterNot { review ->
                        review.id == reviewId
                    },
                    reviewCount = _state.value.reviewCount - 1,
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
}