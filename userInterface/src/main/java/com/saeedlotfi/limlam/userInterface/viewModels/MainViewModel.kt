@file:Suppress("SameParameterValue")

package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.model.StateDoModel
import com.saeedlotfi.limlam.domain.useCase.GetStateUseCase
import com.saeedlotfi.limlam.domain.useCase.SaveStateUseCase
import com.saeedlotfi.limlam.domain.useCase.AddOrRemoveFromFavouritesUseCase
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getStateUseCase: GetStateUseCase,
    private val saveStateUseCase: SaveStateUseCase,
    private val addAndRemoveFromFavouritesUseCase: AddOrRemoveFromFavouritesUseCase
) : ViewModel() {

    init {
        getStateUseCase { state ->
            if (state == null) {
                viewModelScope.launch {
                    saveStateUseCase(
                        StateDoModel(
                            nightMode = false,
                            lightTheme = ThemeManager.Themes.getLightTheme(),
                            darkTheme = ThemeManager.Themes.getDarkTheme(),
                        )
                    ) {
                        ThemeManager.theme = it.lightTheme!!
                    }
                }
            } else ThemeManager.theme = if (state.nightMode)
                state.darkTheme!! else state.lightTheme!!
        }
    }

    fun changeNightMode() {
        viewModelScope.launch {
            getStateUseCase { state ->
                viewModelScope.launch {
                    saveStateUseCase(state!!.also { it.nightMode = !it.nightMode }) {
                        ThemeManager.theme = if (it.nightMode) it.darkTheme!! else it.lightTheme!!
                    }
                }
            }
        }
    }

    fun addOrRemoveFromFavourites(music: MusicDoModel, onComplete: () -> Unit) =
        viewModelScope.launch { addAndRemoveFromFavouritesUseCase(music, onComplete) }

}