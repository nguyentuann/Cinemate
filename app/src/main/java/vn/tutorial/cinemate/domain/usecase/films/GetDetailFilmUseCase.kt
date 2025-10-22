package vn.tutorial.cinemate.domain.usecase.films

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.domain.repository.FilmRepository
import javax.inject.Inject

class GetDetailFilmUseCase @Inject constructor(
    private val filmRepository: FilmRepository
): BaseUseCase<String, Resource<FilmDetailModel?>>() {
    override suspend fun execute(param: String): Resource<FilmDetailModel?> {
        return filmRepository.getDetailFilm(param)
    }
}