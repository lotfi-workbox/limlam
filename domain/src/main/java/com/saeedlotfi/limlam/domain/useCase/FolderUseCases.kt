package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.FolderDoModel
import com.saeedlotfi.limlam.domain.repository.FolderRepository

class GetFolderUseCase(
    private val folderRepository: FolderRepository
) {
    operator fun invoke(id: Long, onComplete: (FolderDoModel?) -> Unit) {
        folderRepository.getFolderFromDb(id, onComplete)
    }
}

class GetFoldersUseCase(
    private val folderRepository: FolderRepository
) {
    operator fun invoke(onComplete: (List<FolderDoModel>?) -> Unit) {
        folderRepository.getFoldersFromDb(onComplete)
    }
}

class SaveFoldersUseCase(
    private val folderRepository: FolderRepository
) {
    suspend operator fun invoke(folders: List<FolderDoModel>, onComplete: () -> Unit) {
        folderRepository.saveFoldersInDb(folders, onComplete)
    }
}

class RemoveFolderUseCase(
    private val folderRepository: FolderRepository
) {
    suspend operator fun invoke(id: Long, onComplete: () -> Unit) {
        folderRepository.removeFolder(id, onComplete)
    }
}