package com.saeedlotfi.limlam.domain.model

data class StateDoModel(

    var playingIndex: Int? = null,

    var queue: MutableList<MusicDoModel>? = null,

    var seekbarPosition: Int? = null,

    var nightMode: Boolean = false,

    var lightTheme: ThemeDoModel? = null,

    var darkTheme: ThemeDoModel? = null,

    var id: Long = DEFAULT_ID

) {
    companion object {
        const val DEFAULT_ID = Long.MAX_VALUE
    }
}