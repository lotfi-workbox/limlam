@file:Suppress("SameParameterValue")

package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.FolderDoModel
import com.saeedlotfi.limlam.domain.model.StateDoModel
import com.saeedlotfi.limlam.domain.useCase.GetFolderUseCase
import com.saeedlotfi.limlam.domain.useCase.GetStateUseCase
import com.saeedlotfi.limlam.domain.useCase.SaveStateUseCase
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreviewerViewModel @Inject constructor(
    private val getStateUseCase: GetStateUseCase,
    private val saveStateUseCase: SaveStateUseCase,
    private val getFolderUseCase: GetFolderUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getStateUseCase {
                viewModelScope.launch {
                    if (it == null) {
                        saveStateUseCase(
                            StateDoModel(
                                nightMode = false,
                                lightTheme = ThemeManager.Themes.getLightTheme(),
                                darkTheme = ThemeManager.Themes.getDarkTheme(),
                            )
                        ) {
                            ThemeManager.theme = it.lightTheme!!
                        }
                    } else ThemeManager.theme = if (it.nightMode)
                        it.darkTheme!! else it.lightTheme!!
                }
            }
        }
    }

    fun getFolderFromDb(id: Long, onComplete: (FolderDoModel?) -> Unit) =
        viewModelScope.launch { getFolderUseCase(id, onComplete) }

}