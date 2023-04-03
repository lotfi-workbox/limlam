package com.saeedlotfi.limlam.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class AlbumDbModel(

    var name : String? = null,

    var artist: String? = null,

    var duration : Int? = null,

    var musics : RealmList<MusicDbModel>? = null,

    var visibility : Boolean = true,

    @PrimaryKey
    var id : Long = System.currentTimeMillis()

) : RealmObject()