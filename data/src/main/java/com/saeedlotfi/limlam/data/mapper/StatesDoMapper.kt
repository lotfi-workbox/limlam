package com.saeedlotfi.limlam.data.mapper

import com.saeedlotfi.limlam.data.model.StatesDbModel
import com.saeedlotfi.limlam.domain.model.StateDoModel
import io.realm.RealmList

fun StateDoModel.asDatabaseModel(): StatesDbModel {
    return StatesDbModel(
        playingIndex = this.playingIndex,
        queue = this.queue?.asDatabaseModel(),
        seekbarPosition = this.seekbarPosition,
        nightMode = this.nightMode,
        lightTheme = this.lightTheme?.asDatabaseModel(),
        darkTheme = this.darkTheme?.asDatabaseModel(),
        id = this.id,
    )
}

fun StatesDbModel.asDomainModel(): StateDoModel {
    return StateDoModel(
        playingIndex = this.playingIndex,
        queue = this.queue?.asDomainModel()?.toMutableList(),
        seekbarPosition = this.seekbarPosition,
        nightMode = this.nightMode,
        lightTheme = this.lightTheme?.asDomainModel(),
        darkTheme = this.darkTheme?.asDomainModel(),
        id = this.id,
    )
}

fun List<StateDoModel>.asDatabaseModel(): RealmList<StatesDbModel> {
    return RealmList<StatesDbModel>().also { list ->
        list.addAll(this.map { it.asDatabaseModel() })
    }
}

fun List<StatesDbModel>.asDomainModel(): List<StateDoModel> = this.map { it.asDomainModel() }



