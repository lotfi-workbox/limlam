package com.saeedlotfi.limlam.data.mapper

import com.saeedlotfi.limlam.data.model.MusicDbModel
import com.saeedlotfi.limlam.domain.model.MusicDoModel
import io.realm.RealmList

fun MusicDoModel.asDatabaseModel(): MusicDbModel {
    return MusicDbModel(
        name = this.name,
        type = this.type,
        path = this.path,
        parent = this.parent,
        parentName = this.parentName,
        title = this.title,
        artist = this.artist,
        album = this.album,
        genre = this.genre,
        duration = this.duration,
        favourite = this.favourite,
        visibility = this.visibility,
        id = this.id
    )
}

fun MusicDbModel.asDomainModel(): MusicDoModel {
    return MusicDoModel(
        name = this.name,
        type = this.type,
        path = this.path ?: "",
        parent = this.parent,
        parentName = this.parentName,
        title = this.title,
        artist = this.artist,
        album = this.album,
        genre = this.genre,
        duration = this.duration,
        favourite = this.favourite,
        visibility = this.visibility,
        id = this.id
    )
}

fun List<MusicDoModel>.asDatabaseModel(): RealmList<MusicDbModel> {
    return RealmList<MusicDbModel>().also { list ->
        list.addAll(this.map { it.asDatabaseModel() })
    }
}

fun List<MusicDbModel>.asDomainModel(): List<MusicDoModel> = this.map { it.asDomainModel() }

