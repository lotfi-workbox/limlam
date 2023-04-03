package com.saeedlotfi.limlam.domain.model

data class MusicDoModel(

    var name: String? = null,

    var type: String?= null,

    var path: String = "",

    var parent: String?= null,

    var parentName: String?= null,

    var artist: String?= null,

    var album: String?= null,

    var genre: String?= null,

    var duration: String?= null,

    var favourite: Boolean = false,

    var visibility: Boolean = true,

    override var title: String?= null,

    override var details: String? = "$artist || $album",

    override var subList: List<AbstractDoModel>? = null,

    override var id: Long = path.hashCode().toLong()

) : AbstractDoModel

