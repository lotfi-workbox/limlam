package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.AlbumDoModel
import com.saeedlotfi.limlam.domain.repository.AlbumRepository

class GetAlbumUseCase(
    private val albumRepository: AlbumRepository
) {
    operator fun invoke(id: Long, onComplete: (AlbumDoModel?) -> Unit) {
        albumRepository.getAlbumFromDb(id, onComplete)
    }
}

class GetAlbumsUseCase(
    private val albumRepository: AlbumRepository
) {
    operator fun invoke(onComplete: (List<AlbumDoModel>?) -> Unit) {
        albumRepository.getAlbumsFromDb(onComplete)
    }
}

class SaveAlbumsUseCase(
    private val albumRepository: AlbumRepository
) {
    suspend operator fun invoke(albums: List<AlbumDoModel>, onComplete: () -> Unit) {
        albumRepository.saveAlbumsInDb(albums, onComplete)
    }
}

class RemoveAlbumUseCase(
    private val albumRepository: AlbumRepository
) {
    suspend operator fun invoke(id: Long, onComplete: () -> Unit) {
        albumRepository.removeAlbum(id, onComplete)
    }
}