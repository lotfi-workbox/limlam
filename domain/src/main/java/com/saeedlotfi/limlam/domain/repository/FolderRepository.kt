package com.saeedlotfi.limlam.domain.repository

import com.saeedlotfi.limlam.domain.model.FolderDoModel

interface FolderRepository {

    fun getFolderFromDb(id: Long, onComplete: (FolderDoModel?) -> Unit)

    fun getFoldersFromDb(onComplete: (List<FolderDoModel>?) -> Unit)

    suspend fun saveFoldersInDb(folders: List<FolderDoModel>, onComplete: () -> Unit)

    suspend fun removeFolder(id: Long, onComplete: () -> Unit)

}