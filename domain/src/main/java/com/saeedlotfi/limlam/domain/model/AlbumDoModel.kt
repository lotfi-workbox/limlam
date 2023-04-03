package com.saeedlotfi.limlam.domain.model

data class AlbumDoModel(

    var name: String? = null,

    var artist: String? = null,

    var musics: MutableList<MusicDoModel>? = null,

    var duration: Int? = null,

    var visibility: Boolean = true,

    override var title: String? = name,

    override var details: String? = artist,

    override var subList: List<AbstractDoModel>? = musics,

    override var id: Long = name.hashCode().toLong()

) : AbstractDoModel