package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SelectMusicsViewModel  @Inject constructor() : ViewModel() {

    var selectedMusics: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel> = mutableListOf()
    var listOfMusicModels : MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel> = mutableListOf()

    override fun onCleared() {
        listOfMusicModels.clear()
        selectedMusics.clear()
        super.onCleared()
    }

}