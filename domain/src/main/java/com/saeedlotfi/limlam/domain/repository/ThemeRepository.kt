package com.saeedlotfi.limlam.domain.repository

import com.saeedlotfi.limlam.domain.model.ThemeDoModel

interface ThemeRepository {

    fun getThemeFromDb(id: Long, onComplete: (ThemeDoModel?) -> Unit)

    fun getThemesFromDb(onComplete: (List<ThemeDoModel>?) -> Unit)

    suspend fun saveThemesInDb(themes: List<ThemeDoModel>, onComplete: () -> Unit)

    suspend fun removeTheme(id: Long, onComplete: () -> Unit)

}