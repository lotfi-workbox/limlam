package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.model.PlayListDoModel
import com.saeedlotfi.limlam.domain.repository.PlayListRepository

class GetPlayListUseCase(
    private val playListRepository: PlayListRepository
) {
    operator fun invoke(id: Long, onComplete: (PlayListDoModel?) -> Unit) {
        playListRepository.getPlayListFromDb(id, onComplete)
    }
}

class GetPlayListsUseCase(
    private val playListRepository: PlayListRepository
) {
    operator fun invoke(onComplete: (List<PlayListDoModel>?) -> Unit) {
        playListRepository.getPlayListsFromDb(onComplete)
    }
}

class SavePlayListsUseCase(
    private val playListRepository: PlayListRepository
) {
    suspend operator fun invoke(playLists: List<PlayListDoModel>, onComplete: () -> Unit) {
        playListRepository.savePlayListsInDb(playLists, onComplete)
    }
}

class RemovePlayListUseCase(
    private val playListRepository: PlayListRepository
) {
    suspend operator fun invoke(id: Long, onComplete: () -> Unit) {
        playListRepository.removePlayList(id, onComplete)
    }
}

class AddToRecentlyPlayedUseCase(
    private val playListRepository: PlayListRepository
) {
    suspend operator fun invoke(music: MusicDoModel, onComplete: () -> Unit) {
        playListRepository.addToRecentlyPlayed(music, onComplete)
    }
}

class AddOrRemoveFromFavouritesUseCase(
    private val playListRepository: PlayListRepository
) {
    suspend operator fun invoke(music: MusicDoModel, onComplete: () -> Unit) {
        playListRepository.addOrRemoveFromFavourites(music, onComplete)
    }
}