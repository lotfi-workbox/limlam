package com.saeedlotfi.limlam.data.mapper

import com.saeedlotfi.limlam.data.model.AlbumDbModel
import com.saeedlotfi.limlam.domain.model.AlbumDoModel
import io.realm.RealmList

fun AlbumDoModel.asDatabaseModel(): AlbumDbModel {
    return AlbumDbModel(
        name = this.name,
        musics = this.musics?.asDatabaseModel(),
        artist = this.artist,
        duration = this.duration,
        visibility = this.visibility,
        id = this.id,
    )
}

fun AlbumDbModel.asDomainModel(): AlbumDoModel {
    return AlbumDoModel(
        name = this.name,
        musics = this.musics?.asDomainModel()?.toMutableList(),
        artist = this.artist,
        duration = this.duration,
        visibility = this.visibility,
        id = this.id,
    )
}

fun List<AlbumDoModel>.asDatabaseModel(): RealmList<AlbumDbModel> {
    return RealmList<AlbumDbModel>().also { list ->
        list.addAll(this.map { it.asDatabaseModel() })
    }
}

fun List<AlbumDbModel>.asDomainModel(): List<AlbumDoModel> = this.map { it.asDomainModel() }


