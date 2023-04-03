package com.saeedlotfi.limlam.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PlayListDbModel(

    var name : String? = null,

    var musics : RealmList<MusicDbModel>? = null,

    var hiddenMusics : RealmList<MusicDbModel>? = null,

    var duration: Int? = null,

    var visibility : Boolean = true,

    @PrimaryKey
    var id : Long = System.currentTimeMillis()

) : RealmObject()