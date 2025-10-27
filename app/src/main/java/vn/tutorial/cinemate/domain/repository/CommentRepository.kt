package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.Comment

interface CommentRepository {
    suspend fun getAllComments(filmId: String): Resource<List<Comment>?>
    suspend fun addComment(filmId: String, stars: Int, content: String): Resource<Comment>

}