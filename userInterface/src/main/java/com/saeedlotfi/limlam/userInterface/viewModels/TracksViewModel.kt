package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.useCase.GetMusicsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class TracksViewModel @Inject constructor(
    private val getMusicsUseCase: GetMusicsUseCase
) : ViewModel() {

    private val _listOfMusics = MutableLiveData<MutableList<MusicDoModel>>()
    val listOfMusics: LiveData<MutableList<MusicDoModel>>
        get() = _listOfMusics

    init {
        refreshView()
    }

    fun refreshView() {
        viewModelScope.launch {
            getMusicsUseCase {
                _listOfMusics.postValue(it?.toMutableList())
            }
        }
    }

}
