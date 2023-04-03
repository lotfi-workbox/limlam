package com.saeedlotfi.limlam.data.mapper

import com.saeedlotfi.limlam.data.model.FolderDbModel
import com.saeedlotfi.limlam.domain.model.FolderDoModel
import io.realm.RealmList

fun FolderDoModel.asDatabaseModel(): FolderDbModel {
    return FolderDbModel(
        name = this.name,
        musics = this.musics?.asDatabaseModel(),
        path = this.path,
        visibility = this.visibility,
        id = this.id,
    )
}

fun FolderDbModel.asDomainModel(): FolderDoModel {
    return FolderDoModel(
        name = this.name,
        musics = this.musics?.asDomainModel()?.toMutableList(),
        path = this.path,
        visibility = this.visibility,
        id = this.id,
    )
}

fun List<FolderDoModel>.asDatabaseModel(): RealmList<FolderDbModel> {
    return RealmList<FolderDbModel>().also { list ->
        list.addAll(this.map { it.asDatabaseModel() })
    }
}

fun List<FolderDbModel>.asDomainModel(): List<FolderDoModel> = this.map { it.asDomainModel() }



