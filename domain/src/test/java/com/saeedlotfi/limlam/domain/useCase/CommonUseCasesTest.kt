package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.StateDoModel
import com.saeedlotfi.limlam.domain.repository.CommonRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CommonUseCasesTest {

    private lateinit var commonRepository: CommonRepository
    private lateinit var getStateUseCase: GetStateUseCase
    private lateinit var saveStateUseCase: SaveStateUseCase

    private val statesModel = StateDoModel()

    @Before
    fun beforeTest() {
        commonRepository = mock()

        getStateUseCase = GetStateUseCase(commonRepository)
        saveStateUseCase = SaveStateUseCase(commonRepository)
    }

    @Test
    fun test_getStateUseCase_whenSuccess_returnsState() {
        val callback: (StateDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], statesModel) }

        given(commonRepository.getStateFromDb(StateDoModel.DEFAULT_ID, callback))
            .willAnswer { callback.invoke(statesModel) }

        getStateUseCase(callback)

        verify(callback).invoke(any())
    }

    @Test
    fun test_getStateUseCase_whenFail_returnsNull() {
        val callback: (StateDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], null) }

        given(commonRepository.getStateFromDb(StateDoModel.DEFAULT_ID, callback))
            .willAnswer { callback.invoke(null) }

        getStateUseCase(callback)

        verify(callback).invoke(anyOrNull())
    }

    @Test
    fun test_saveStateUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: (StateDoModel) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], statesModel) }

        given(commonRepository.saveStateInDb(any(), eq(callback)))
            .willAnswer { callback.invoke(it.arguments[0] as StateDoModel) }

        saveStateUseCase(statesModel, callback)

        verify(callback).invoke(any())
    }

    @Test(expected = Exception::class)
    fun test_addOrRemoveFromFavouritesUseCase_whenFail_throwsException() = runTest {
        val callback: (StateDoModel) -> Unit = mock()
        val exception = Exception("State Was Not Saved")

        given(callback.invoke(any())).willAnswer { assert(false) }

        given(commonRepository.saveStateInDb(any(), eq(callback)))
            .willThrow(exception)

        saveStateUseCase(statesModel, callback)

        verify(callback).invoke(any())
    }

}