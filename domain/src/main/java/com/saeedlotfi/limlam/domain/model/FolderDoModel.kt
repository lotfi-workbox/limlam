package com.saeedlotfi.limlam.domain.model

data class FolderDoModel(

    var name: String? = null,

    var path: String? = null,

    var musics: MutableList<MusicDoModel>? = null,

    var visibility: Boolean = true,

    override var details: String? = null,

    override var title: String? = name,

    override var subList: List<AbstractDoModel>? = musics,

    override var id: Long = path.hashCode().toLong()
) : AbstractDoModel