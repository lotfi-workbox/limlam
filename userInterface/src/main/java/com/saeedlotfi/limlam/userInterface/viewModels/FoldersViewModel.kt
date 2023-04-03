package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.FolderDoModel
import com.saeedlotfi.limlam.domain.useCase.GetFoldersUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FoldersViewModel @Inject constructor(
    private val getFoldersUseCase: GetFoldersUseCase
) : ViewModel() {

    private val _listOfFolders = MutableLiveData<MutableList<FolderDoModel>?>()
    val listOfFolders: LiveData<MutableList<FolderDoModel>?>
        get() = _listOfFolders

    init {
        refreshView()
    }

    fun refreshView() {
        viewModelScope.launch {
            getFoldersUseCase {
                _listOfFolders.postValue(it?.toMutableList())
            }
        }
    }
}