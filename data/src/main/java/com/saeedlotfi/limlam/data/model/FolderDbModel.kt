package com.saeedlotfi.limlam.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FolderDbModel (

    var name : String? = null,

    var path : String? = null,

    var musics : RealmList<MusicDbModel>? = null,

    var visibility : Boolean = true,

    @PrimaryKey
    var id : Long = System.currentTimeMillis()

) : RealmObject()