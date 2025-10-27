package vn.tutorial.cinemate.domain.usecase.comments

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.Comment
import vn.tutorial.cinemate.domain.repository.CommentRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
): BaseUseCase<AddCommentUseCase.Params, Resource<Comment>>() {
    data class Params(
        val filmId: String,
        val content: String,
        val stars: Int
    )

    override suspend fun execute(param: Params): Resource<Comment> {
        return commentRepository.addComment(
            filmId = param.filmId,
            stars = param.stars,
            content = param.content,
        )
    }
}