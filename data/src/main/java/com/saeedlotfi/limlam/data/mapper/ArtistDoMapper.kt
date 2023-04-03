package com.saeedlotfi.limlam.data.mapper

import com.saeedlotfi.limlam.data.model.ArtistDbModel
import com.saeedlotfi.limlam.domain.model.ArtistDoModel
import io.realm.RealmList

fun ArtistDoModel.asDatabaseModel(): ArtistDbModel {
    return ArtistDbModel(
        name = this.name,
        albums = this.albums?.asDatabaseModel(),
        allTracks = this.allTracks?.asDatabaseModel(),
        duration = this.duration,
        visibility = this.visibility,
        id = this.id,
    )
}

fun ArtistDbModel.asDomainModel(): ArtistDoModel {
    return ArtistDoModel(
        name = this.name,
        albums = this.albums?.asDomainModel()?.toMutableList(),
        allTracks = this.allTracks?.asDomainModel()?.toMutableList(),
        duration = this.duration,
        visibility = this.visibility,
        id = this.id,
    )
}

fun List<ArtistDoModel>.asDatabaseModel(): RealmList<ArtistDbModel> {
    return RealmList<ArtistDbModel>().also { list ->
        list.addAll(this.map { it.asDatabaseModel() })
    }
}

fun List<ArtistDbModel>.asDomainModel(): List<ArtistDoModel> = this.map { it.asDomainModel() }



