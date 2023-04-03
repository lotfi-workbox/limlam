package com.saeedlotfi.limlam.domain.repository

import com.saeedlotfi.limlam.domain.model.StateDoModel

interface CommonRepository {

    fun getStateFromDb(id: Long, onComplete: (StateDoModel?) -> Unit)

    suspend fun saveStateInDb(state: StateDoModel, onComplete: (StateDoModel) -> Unit)

}