package vn.tutorial.cinemate.domain.usecase.search

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.domain.repository.FilmRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) :
    BaseUseCase<SearchUseCase.Params, Resource<List<FilmDetailModel>?>>() {

    data class Params(
        val query: String
    )

    override suspend fun execute(param: Params): Resource<List<FilmDetailModel>?> {
        return filmRepository.searchFilms(param.query)
    }
}