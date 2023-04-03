package com.saeedlotfi.limlam.domain.repository

import com.saeedlotfi.limlam.domain.model.MusicDoModel

interface MusicRepository {

    fun getMusicFromDb(id: Long, onComplete: (MusicDoModel?) -> Unit)

    fun getMusicsFromDb(onComplete: (List<MusicDoModel>?) -> Unit)

    suspend fun saveMusicsInDb(
        musics: List<MusicDoModel>,
        onComplete: (newSaved : MutableList<MusicDoModel>) -> Unit
    )

    suspend fun removeMusic(id: Long, onComplete: () -> Unit)

}