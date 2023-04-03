package com.saeedlotfi.limlam.domain.model

interface AbstractDoModel {

    var title: String?

    var details: String?

    var subList: List<AbstractDoModel>?

    var id: Long

    companion object {
        fun createInstance(
            title: String? = null,
            details: String? = null,
            subList: List<AbstractDoModel>? = null
        ): AbstractDoModel {
            return object :
                AbstractDoModel {
                override var title: String? = title
                override var details: String? = details
                override var subList: List<AbstractDoModel>? = subList
                override var id: Long = System.currentTimeMillis()
            }
        }
    }

}