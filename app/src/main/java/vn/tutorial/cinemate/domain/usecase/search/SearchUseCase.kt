package vn.tutorial.cinemate.domain.usecase.search

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val filmRepository: MovieRepository
) :
    BaseUseCase<SearchUseCase.Params, Resource<List<MovieDetailModel>?>>() {

    data class Params(
        val query: String
    )

    override suspend fun execute(param: Params): Resource<List<MovieDetailModel>?> {
        return filmRepository.searchFilms(param.query)
    }
}