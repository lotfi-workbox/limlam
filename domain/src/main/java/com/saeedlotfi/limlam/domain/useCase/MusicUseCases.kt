package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.repository.MusicRepository

class GetMusicUseCase(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(id: Long, onComplete: (MusicDoModel?) -> Unit) {
        musicRepository.getMusicFromDb(id, onComplete)
    }
}

class GetMusicsUseCase(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(onComplete: (List<MusicDoModel>?) -> Unit) {
        musicRepository.getMusicsFromDb(onComplete)
    }
}

class SaveMusicsUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(
        musics: List<MusicDoModel>,
        onComplete: (newSaved: MutableList<MusicDoModel>) -> Unit
    ) {
        musicRepository.saveMusicsInDb(musics, onComplete)
    }
}

class RemoveMusicUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(id: Long, onComplete: () -> Unit) {
        musicRepository.removeMusic(id, onComplete)
    }
}