package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.GenreDoModel
import com.saeedlotfi.limlam.domain.repository.GenreRepository

class GetGenreUseCase(
    private val genreRepository: GenreRepository
) {
    operator fun invoke(id: Long, onComplete: (GenreDoModel?) -> Unit) {
        genreRepository.getGenreFromDb(id, onComplete)
    }
}

class GetGenresUseCase(
    private val genreRepository: GenreRepository
) {
    operator fun invoke(onComplete: (List<GenreDoModel>?) -> Unit) {
        genreRepository.getGenresFromDb(onComplete)
    }
}

class SaveGenresUseCase(
    private val genreRepository: GenreRepository
) {
    suspend operator fun invoke(genres: List<GenreDoModel>, onComplete: () -> Unit) {
        genreRepository.saveGenresInDb(genres, onComplete)
    }
}

class RemoveGenreUseCase(
    private val genreRepository: GenreRepository
) {
    suspend operator fun invoke(id: Long, onComplete: () -> Unit) {
        genreRepository.removeGenre(id, onComplete)
    }
}