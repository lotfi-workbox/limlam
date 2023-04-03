package com.saeedlotfi.limlam.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ArtistDbModel(

    var name : String? = null,

    var duration : Int? = null,

    var albums : RealmList<AlbumDbModel>? = null,

    var allTracks : RealmList<MusicDbModel>? = null,

    var visibility : Boolean = true,

    @PrimaryKey
    var id : Long = System.currentTimeMillis()

) : RealmObject()
