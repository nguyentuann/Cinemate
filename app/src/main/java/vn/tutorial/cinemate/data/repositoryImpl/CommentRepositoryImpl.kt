package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.domain.model.Comment
import vn.tutorial.cinemate.domain.repository.CommentRepository
import vn.tutorial.cinemate.mockdata.commentsData
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(): CommentRepository, BaseService() {
    override suspend fun getAllComments(filmId: String): Resource<List<Comment>?> {
        return Resource.Success(commentsData)
    }

    override suspend fun addComment(filmId: String, stars: Int, content: String): Resource<Comment> {
        return Resource.Success(commentsData[1])
    }
}