package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.ArtistDoModel
import com.saeedlotfi.limlam.domain.repository.ArtistRepository

class GetArtistUseCase(
    private val artistRepository: ArtistRepository
) {
    operator fun invoke(id: Long, onComplete: (ArtistDoModel?) -> Unit) {
        artistRepository.getArtistFromDb(id, onComplete)
    }
}

class GetArtistsUseCase(
    private val artistRepository: ArtistRepository
) {
    operator fun invoke(onComplete: (List<ArtistDoModel>?) -> Unit) {
        artistRepository.getArtistsFromDb(onComplete)
    }
}

class SaveArtistsUseCase(
    private val artistRepository: ArtistRepository
) {
    suspend operator fun invoke(artists: List<ArtistDoModel>, onComplete: () -> Unit) {
        artistRepository.saveArtistsInDb(artists, onComplete)
    }
}

class RemoveArtistUseCase(
    private val artistRepository: ArtistRepository
) {
    suspend operator fun invoke(id: Long, onComplete: () -> Unit) {
        artistRepository.removeArtist(id, onComplete)
    }
}