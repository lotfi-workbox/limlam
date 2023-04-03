package com.saeedlotfi.limlam.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MusicDbModel(

    var name: String? = null,

    var type: String? = null,

    var path: String? = null,

    var parent: String? = null,

    var parentName: String? = null,

    var title: String? = null,

    var artist: String? = null,

    var album: String? = null,

    var genre: String? = null,

    var duration: String? = null,

    var favourite: Boolean = false,

    var visibility: Boolean = true,

    @PrimaryKey
    var id: Long = System.currentTimeMillis()

) : RealmObject()