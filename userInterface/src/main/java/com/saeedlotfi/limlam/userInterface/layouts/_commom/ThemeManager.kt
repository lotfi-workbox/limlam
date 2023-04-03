package com.saeedlotfi.limlam.userInterface.layouts._commom

import com.saeedlotfi.limlam.userInterface._common.MultiViewRvAdapter
import com.saeedlotfi.limlam.userInterface.R

object ThemeManager {

    private val listeners = mutableSetOf<ThemeChangedListener>()
    var theme = com.saeedlotfi.limlam.domain.model.ThemeDoModel()
        set(value) {
            field = value
            listeners.forEach { listener -> listener.onThemeChanged(value) }
        }

    interface ThemeChangedListener {
        fun onThemeChanged(theme: com.saeedlotfi.limlam.domain.model.ThemeDoModel)
    }

    fun addListener(listener: ThemeChangedListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ThemeChangedListener) {
        listeners.remove(listener)
    }

    fun removeAllListeners(){
        listeners.clear()
    }

    class Themes {
        companion object {

            fun getLightTheme(): com.saeedlotfi.limlam.domain.model.ThemeDoModel {
                return com.saeedlotfi.limlam.domain.model.ThemeDoModel(
                    name = "Light",
                    backgrounds = "#F8F8F8",
                    paleBackgrounds = "#EFEFEF",
                    windowBackground = "#FDFDFD",
                    textsAndIcons = "#1C1C1C",
                    textHint = "#74000000",
                    strokeColor = "#FFFFFF",
                    topOfWindows = "#424A60",
                    topOfWindowTexts = "#FFFFFF",
                    undoBar = "#222222",
                    startShadow = "#00000000",
                    endShadow = "#2C000000",
                    rippleColor = "#BE808080",
                    id = System.currentTimeMillis() + 1
                )
            }

            fun getDarkTheme(): com.saeedlotfi.limlam.domain.model.ThemeDoModel {
                return com.saeedlotfi.limlam.domain.model.ThemeDoModel(
                    name = "Dark",
                    backgrounds = "#2E2E2E",
                    paleBackgrounds = "#494848",
                    windowBackground = "#1B1B1B",
                    textsAndIcons = "#ECECEC",
                    textHint = "#838383",
                    strokeColor = "#090909",
                    topOfWindows = "#222222",
                    topOfWindowTexts = "#FFFFFF",
                    undoBar = "#222222",
                    startShadow = "#00000000",
                    endShadow = "#2C000000",
                    rippleColor = "#BE808080",
                    id = System.currentTimeMillis() + 2
                )
            }

        }
    }

    fun MultiViewRvAdapter.Companion.getDrawableId(item: com.saeedlotfi.limlam.domain.model.AbstractDoModel?): Int {
        if (item == null) return R.drawable.ic_bug
        return when {
            item::class.java == com.saeedlotfi.limlam.domain.model.MusicDoModel::class.java -> R.drawable.ic_note
            item::class.java == com.saeedlotfi.limlam.domain.model.FolderDoModel::class.java -> R.drawable.ic_folder
            item::class.java == com.saeedlotfi.limlam.domain.model.GenreDoModel::class.java -> R.drawable.ic_genres
            item::class.java == com.saeedlotfi.limlam.domain.model.AlbumDoModel::class.java -> R.drawable.ic_albums
            item::class.java == com.saeedlotfi.limlam.domain.model.ArtistDoModel::class.java -> R.drawable.ic_artists
            item::class.java == com.saeedlotfi.limlam.domain.model.PlayListDoModel::class.java -> R.drawable.ic_playlist
            item.title == "Create Play List" -> R.drawable.ic_plus
            else -> R.drawable.ic_bug
        }
    }

    fun MultiViewRvAdapter.Companion.getModeByModelClass(targetClass: Class<*>?): MultiViewRvAdapter.Mode {
        return when (targetClass) {
            com.saeedlotfi.limlam.domain.model.MusicDoModel::class.java -> MultiViewRvAdapter.Mode.LINEAR_ONE_PICTURE
            com.saeedlotfi.limlam.domain.model.FolderDoModel::class.java -> MultiViewRvAdapter.Mode.LINEAR_ONE_PICTURE
            com.saeedlotfi.limlam.domain.model.GenreDoModel::class.java -> MultiViewRvAdapter.Mode.LINEAR_FOUR_PICTURE
            com.saeedlotfi.limlam.domain.model.AlbumDoModel::class.java -> MultiViewRvAdapter.Mode.GRID
            com.saeedlotfi.limlam.domain.model.ArtistDoModel::class.java -> MultiViewRvAdapter.Mode.LINEAR_ONE_PICTURE
            com.saeedlotfi.limlam.domain.model.PlayListDoModel::class.java -> MultiViewRvAdapter.Mode.LINEAR_FOUR_PICTURE
            else -> throw Exception("there is no drawable for this class")
        }
    }

}

