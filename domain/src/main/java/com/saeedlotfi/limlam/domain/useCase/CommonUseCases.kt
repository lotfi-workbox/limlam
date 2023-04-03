package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.StateDoModel
import com.saeedlotfi.limlam.domain.repository.CommonRepository

class GetStateUseCase(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(onComplete: (StateDoModel?) -> Unit) {
        commonRepository.getStateFromDb(StateDoModel.DEFAULT_ID, onComplete)
    }
}

class SaveStateUseCase(
    private val commonRepository: CommonRepository
) {
    suspend operator fun invoke(
        state: StateDoModel,
        onComplete: (StateDoModel) -> Unit
    ) {
        commonRepository.saveStateInDb(state, onComplete)
    }
}
