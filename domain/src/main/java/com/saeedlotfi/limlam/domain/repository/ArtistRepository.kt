package com.saeedlotfi.limlam.domain.repository

import com.saeedlotfi.limlam.domain.model.ArtistDoModel


interface ArtistRepository {

    fun getArtistFromDb(id: Long, onComplete: (ArtistDoModel?) -> Unit)

    fun getArtistsFromDb(onComplete: (List<ArtistDoModel>?) -> Unit)

    suspend fun saveArtistsInDb(artists: List<ArtistDoModel>, onComplete: () -> Unit)

    suspend fun removeArtist(id: Long, onComplete: () -> Unit)

}