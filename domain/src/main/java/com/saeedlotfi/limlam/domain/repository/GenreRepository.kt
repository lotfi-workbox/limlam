package com.saeedlotfi.limlam.domain.repository

import com.saeedlotfi.limlam.domain.model.GenreDoModel

interface GenreRepository {

    fun getGenreFromDb(id: Long, onComplete: (GenreDoModel?) -> Unit)

    fun getGenresFromDb(onComplete: (List<GenreDoModel>?) -> Unit)

    suspend fun saveGenresInDb(genres: List<GenreDoModel>, onComplete: () -> Unit)

    suspend fun removeGenre(id: Long, onComplete: () -> Unit)

}