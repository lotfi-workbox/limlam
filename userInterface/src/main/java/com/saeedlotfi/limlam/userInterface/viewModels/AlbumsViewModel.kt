package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.AlbumDoModel
import com.saeedlotfi.limlam.domain.useCase.GetAlbumsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumsViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private val _listOfAlbums = MutableLiveData<MutableList<AlbumDoModel>?>()
    val listOfAlbums: LiveData<MutableList<AlbumDoModel>?>
        get() = _listOfAlbums

    init {
        refreshView()
    }

    fun refreshView() {
        viewModelScope.launch {
            getAlbumsUseCase {
                _listOfAlbums.postValue(it?.toMutableList())
            }
        }
    }

}

