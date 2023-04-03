package com.saeedlotfi.limlam.data.mapper

import com.saeedlotfi.limlam.data.model.PlayListDbModel
import com.saeedlotfi.limlam.domain.model.PlayListDoModel
import io.realm.RealmList

fun PlayListDoModel.asDatabaseModel(): PlayListDbModel {
    return PlayListDbModel(
        name = this.name,
        musics = this.musics?.asDatabaseModel(),
        hiddenMusics = this.hiddenMusics?.asDatabaseModel(),
        duration = this.duration,
        visibility = this.visibility,
        id = this.id,
    )
}

fun PlayListDbModel.asDomainModel(): PlayListDoModel {
    return PlayListDoModel(
        name = this.name,
        musics = this.musics?.asDomainModel()?.toMutableList(),
        hiddenMusics = this.hiddenMusics?.asDomainModel()?.toMutableList(),
        duration = this.duration,
        visibility = this.visibility,
        id = this.id,
    )
}

fun List<PlayListDoModel>.asDatabaseModel(): RealmList<PlayListDbModel> {
    return RealmList<PlayListDbModel>().also { list ->
        list.addAll(this.map { it.asDatabaseModel() })
    }
}

fun List<PlayListDbModel>.asDomainModel(): List<PlayListDoModel> = this.map { it.asDomainModel() }



