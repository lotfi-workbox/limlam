package com.saeedlotfi.limlam.userInterface._common


import android.os.Environment

object Constants {

    const val UNKNOWN = "UNKNOWN"

    const val BC_ACTION_REFRESH_MAIN = "BC_ACTION_REFRESH_MAIN"

    const val BC_ACTION_REFRESH_FOLDERS = "BC_ACTION_REFRESH_FOLDERS"

    const val BC_ACTION_REFRESH_GENRES = "BC_ACTION_REFRESH_GENRES"

    const val BC_ACTION_REFRESH_ALBUMS = "BC_ACTION_REFRESH_ALBUMS"

    const val BC_ACTION_REFRESH_ARTISTS = "BC_ACTION_REFRESH_ARTISTS"

    const val BC_ACTION_REFRESH_PLAYLISTS = "BC_ACTION_REFRESH_PLAYLISTS"

    const val BC_KEY_REFRESH_FAVOURITES = "BC_KEY_REFRESH_FAVOURITES"

    const val BC_KEY_REFRESH_RECENTLY_PLAYED = "BC_KEY_REFRESH_RECENTLY_PLAYED"

    const val packageName = "com.saeedlotfi.limlam"

    val cachePath: String = Environment.getDataDirectory().toString() + "/data/$packageName/cache/"

}