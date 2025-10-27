package vn.tutorial.cinemate.domain.usecase.comments

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.Comment
import vn.tutorial.cinemate.domain.repository.CommentRepository
import vn.tutorial.cinemate.domain.repository.FilmRepository
import javax.inject.Inject

class GetAllCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) : BaseUseCase<String, Resource<List<Comment>?>>() {
    override suspend fun execute(param: String): Resource<List<Comment>?> {
        return commentRepository.getAllComments(param)
    }
}