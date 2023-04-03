package com.saeedlotfi.limlam.domain.model

data class ArtistDoModel(

    var name: String? = null,

    var albums: MutableList<AlbumDoModel>? = null,

    var allTracks: MutableList<MusicDoModel>? = null,

    var duration: Int? = null,

    var visibility: Boolean = true,

    override var title: String? = name,

    override var details: String? = null,

    override var subList: List<AbstractDoModel>? = albums,

    override var id: Long = name.hashCode().toLong()

) : AbstractDoModel