package com.saeedlotfi.limlam.data.mapper

import com.saeedlotfi.limlam.data.model.GenreDbModel
import com.saeedlotfi.limlam.domain.model.GenreDoModel
import io.realm.RealmList

fun GenreDoModel.asDatabaseModel(): GenreDbModel {
    return GenreDbModel(
        name = this.name,
        musics = this.musics?.asDatabaseModel(),
        duration = this.duration,
        visibility = this.visibility,
        id = this.id,
    )
}

fun GenreDbModel.asDomainModel(): GenreDoModel {
    return GenreDoModel(
        name = this.name,
        musics = this.musics?.asDomainModel()?.toMutableList(),
        duration = this.duration,
        visibility = this.visibility,
        id = this.id,
    )
}

fun List<GenreDoModel>.asDatabaseModel(): RealmList<GenreDbModel> {
    return RealmList<GenreDbModel>().also { list ->
        list.addAll(this.map { it.asDatabaseModel() })
    }
}

fun List<GenreDbModel>.asDomainModel(): List<GenreDoModel> = this.map { it.asDomainModel() }



