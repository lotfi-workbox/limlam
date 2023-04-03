package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.ThemeDoModel
import com.saeedlotfi.limlam.domain.repository.ThemeRepository

class GetThemeUseCase(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(id: Long, onComplete: (ThemeDoModel?) -> Unit) {
        themeRepository.getThemeFromDb(id, onComplete)
    }
}

class GetThemesUseCase(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(onComplete: (List<ThemeDoModel>?) -> Unit) {
        themeRepository.getThemesFromDb(onComplete)
    }
}

class SaveThemesUseCase(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(themes: List<ThemeDoModel>, onComplete: () -> Unit) {
        themeRepository.saveThemesInDb(themes, onComplete)
    }
}

class RemoveThemeUseCase(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(id: Long, onComplete: () -> Unit) {
        themeRepository.removeTheme(id, onComplete)
    }
}