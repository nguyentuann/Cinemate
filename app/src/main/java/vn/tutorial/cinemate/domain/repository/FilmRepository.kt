package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.FilmDetailModel

interface FilmRepository {
    suspend fun searchFilms(query: String): Resource<List<FilmDetailModel>?>
    suspend fun getTrendingFilms(): Resource<List<FilmDetailModel>?>
}