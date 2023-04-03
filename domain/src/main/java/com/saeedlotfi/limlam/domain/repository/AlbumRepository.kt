package com.saeedlotfi.limlam.domain.repository

import com.saeedlotfi.limlam.domain.model.AlbumDoModel

interface AlbumRepository {

    fun getAlbumFromDb(id: Long, onComplete: (AlbumDoModel?) -> Unit)

    fun getAlbumsFromDb(onComplete: (List<AlbumDoModel>?) -> Unit)

    suspend fun saveAlbumsInDb(albums: List<AlbumDoModel>, onComplete: () -> Unit)

    suspend fun removeAlbum(id: Long, onComplete: () -> Unit)

}