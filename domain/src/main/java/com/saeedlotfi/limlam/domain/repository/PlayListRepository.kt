package com.saeedlotfi.limlam.domain.repository

import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.model.PlayListDoModel

interface PlayListRepository {

    fun getPlayListFromDb(id: Long, onComplete: (PlayListDoModel?) -> Unit)

    fun getPlayListsFromDb(onComplete: (List<PlayListDoModel>?) -> Unit)

    suspend fun savePlayListsInDb(playlists: List<PlayListDoModel>, onComplete: () -> Unit)

    suspend fun removePlayList(id: Long, onComplete: () -> Unit)

    suspend fun addToRecentlyPlayed(music: MusicDoModel, onComplete: () -> Unit)

    suspend fun addOrRemoveFromFavourites(music: MusicDoModel, onComplete: () -> Unit)

    enum class Defaults(val displayedName: String) {

        Favourites("Favourites"),

        RecentlyPlayed("Recently Played")

    }

}