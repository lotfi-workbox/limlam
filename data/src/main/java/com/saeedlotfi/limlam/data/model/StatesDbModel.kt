package com.saeedlotfi.limlam.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class StatesDbModel(

    var playingIndex: Int? = null,

    var queue: RealmList<MusicDbModel>? = null,

    var seekbarPosition: Int? = null,

    var nightMode: Boolean = false,

    var lightTheme: ThemeDbModel? = null,

    var darkTheme: ThemeDbModel? = null,

    @PrimaryKey
    var id: Long = System.currentTimeMillis()

) : RealmObject()