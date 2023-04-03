package com.saeedlotfi.limlam.userInterface.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.limlam.domain.model.GenreDoModel
import com.saeedlotfi.limlam.domain.useCase.GetGenresUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GenresViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    private val _listOfGenres = MutableLiveData<MutableList<GenreDoModel>?>()
    val listOfGenres: LiveData<MutableList<GenreDoModel>?>
        get() = _listOfGenres

    init {
        refreshView()
    }

    fun refreshView() {
        viewModelScope.launch {
            getGenresUseCase {
                _listOfGenres.postValue(it?.toMutableList())
            }
        }
    }

}
