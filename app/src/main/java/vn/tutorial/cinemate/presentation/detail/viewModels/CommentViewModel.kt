package vn.tutorial.cinemate.presentation.detail.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.Comment
import vn.tutorial.cinemate.domain.usecase.comments.AddCommentUseCase
import vn.tutorial.cinemate.domain.usecase.comments.GetAllCommentsUseCase
import vn.tutorial.cinemate.mockdata.commentsData
import javax.inject.Inject

data class CommentUIState(
    var comments: List<Comment> = emptyList(),
    val error: String? = null,
    val loading: Boolean = false
)

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val getAllCommentsUseCase: GetAllCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(CommentUIState())
    val state: StateFlow<CommentUIState> = _state


    fun updateComments(cmt: Comment) {
        val newComments = _state.value.comments + listOf(cmt)
        _state.value.comments = newComments
    }

    fun getAllComments(filmId: String) {
            executeUseCase(
                state = _state,
                block = {
                    getAllCommentsUseCase(filmId)
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

    fun addComment(filmId: String, stars: Int, content: String) {
        executeUseCase(
            state = _state,
            block = {
                addCommentUseCase(
                    AddCommentUseCase.Params(
                        filmId = filmId,
                        stars = stars,
                        content = content
                    )
                )
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

    fun updateComment(filmId: String, comment: Comment) {

    }

    fun deleteComment(filmId: String, commentId: Int) {

    }
}