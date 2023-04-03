package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.model.PlayListDoModel
import com.saeedlotfi.limlam.domain.repository.PlayListRepository
import com.saeedlotfi.limlam.domain.useCase.GetMusicsUseCase
import com.saeedlotfi.limlam.domain.useCase.GetPlayListUseCase
import com.saeedlotfi.limlam.domain.useCase.GetPlayListsUseCase
import com.saeedlotfi.limlam.domain.useCase.SavePlayListsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayListsViewModel @Inject constructor(
    private val getMusicsUseCase: GetMusicsUseCase,
    private val getPlayListsUseCase: GetPlayListsUseCase,
    private val getPlayListUseCase: GetPlayListUseCase,
    private val savePlayListsUseCase: SavePlayListsUseCase
) : ViewModel() {

    private val _listOfPlayLists = MutableLiveData<MutableList<PlayListDoModel>?>()
    val listOfPlayLists: LiveData<MutableList<PlayListDoModel>?>
        get() = _listOfPlayLists

    private val _favourites = MutableLiveData<PlayListDoModel>()
    val favourites: LiveData<PlayListDoModel>
        get() = _favourites

    private val _recentlyPlayed = MutableLiveData<PlayListDoModel>()
    val recentlyPlayed: LiveData<PlayListDoModel>
        get() = _recentlyPlayed

    var listOfMusics: MutableList<MusicDoModel>? = mutableListOf()
        private set

    init {
        refreshView()
    }

    fun refreshView() {
        viewModelScope.launch {
            getPlayListsUseCase {
                _listOfPlayLists.postValue(it?.toMutableList())
            }
            getMusicsUseCase {
                listOfMusics = it?.toMutableList()
            }
        }
    }

    fun refreshFavourites() {
        viewModelScope.launch {
            getPlayListUseCase(
                PlayListRepository.Defaults.Favourites.name.hashCode().toLong()
            ) {
                _favourites.postValue(it)
            }
        }
    }

    fun refreshRecentlyPlayed() {
        viewModelScope.launch {
            getPlayListUseCase(
                PlayListRepository.Defaults.RecentlyPlayed.name.hashCode().toLong()
            ) {
                _recentlyPlayed.postValue(it)
            }
        }
    }

    fun savePlayListsInDb(playlists: List<PlayListDoModel>, onComplete: () -> Unit) =
        viewModelScope.launch { savePlayListsUseCase(playlists, onComplete) }

}