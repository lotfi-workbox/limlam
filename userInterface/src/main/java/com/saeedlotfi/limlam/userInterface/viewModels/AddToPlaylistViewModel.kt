package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.PlayListDoModel
import com.saeedlotfi.limlam.domain.useCase.GetPlayListsUseCase
import com.saeedlotfi.limlam.domain.useCase.SavePlayListsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddToPlaylistViewModel @Inject constructor(
    private val getPlayListsUseCase: GetPlayListsUseCase,
    private val savePlayListsUseCase: SavePlayListsUseCase
) : ViewModel() {

    private val _listOfPlayLists = MutableLiveData<MutableList<PlayListDoModel>?>()
    val listOfPlayLists: LiveData<MutableList<PlayListDoModel>?>
        get() = _listOfPlayLists

    init {
        refreshView()
    }

    fun refreshView() {
        viewModelScope.launch {
            getPlayListsUseCase {
                _listOfPlayLists.postValue(it?.toMutableList())
            }
        }
    }

    fun savePlayListsInDb(playlists: List<PlayListDoModel>, onComplete: () -> Unit) =
        viewModelScope.launch { savePlayListsUseCase(playlists, onComplete) }

}
