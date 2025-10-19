package vn.tutorial.cinemate.domain.usecase.films

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.domain.repository.FilmRepository
import javax.inject.Inject

class GetSectionFilmsUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : BaseUseCase<Unit, Resource<Map<String, List<FilmDetailModel>>?>>() {
    override suspend fun execute(param: Unit): Resource<Map<String, List<FilmDetailModel>>?> {
        return filmRepository.getSectionFilms()
    }
}