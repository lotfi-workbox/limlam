package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.ArtistDoModel
import com.saeedlotfi.limlam.domain.useCase.GetArtistsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArtistsViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase
) : ViewModel() {

    private val _listOfArtists = MutableLiveData<MutableList<ArtistDoModel>?>()
    val listOfArtists: LiveData<MutableList<ArtistDoModel>?>
        get() = _listOfArtists

    init {
        refreshView()
    }

    fun refreshView() {
        viewModelScope.launch {
            getArtistsUseCase {
                _listOfArtists.postValue(it?.toMutableList())
            }
        }
    }

}

