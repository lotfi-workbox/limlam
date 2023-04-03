package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.repository.MusicRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.anyList

@OptIn(ExperimentalCoroutinesApi::class)
class MusicUseCasesTest {

    private lateinit var musicRepository: MusicRepository
    private lateinit var getMusicUseCase: GetMusicUseCase
    private lateinit var getMusicsUseCase: GetMusicsUseCase
    private lateinit var saveMusicsUseCase: SaveMusicsUseCase
    private lateinit var removeMusicUseCase: RemoveMusicUseCase

    private val musicModel = MusicDoModel()

    @Before
    fun beforeTest() {
        musicRepository = mock()

        getMusicUseCase = GetMusicUseCase(musicRepository)
        getMusicsUseCase = GetMusicsUseCase(musicRepository)
        saveMusicsUseCase = SaveMusicsUseCase(musicRepository)
        removeMusicUseCase = RemoveMusicUseCase(musicRepository)
    }

    @Test
    fun test_getMusicUseCase_whenSuccess_returnsMusic() {
        val callback: (MusicDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], musicModel) }

        given(musicRepository.getMusicFromDb(musicModel.id, callback))
            .willAnswer { callback.invoke(musicModel) }

        getMusicUseCase(musicModel.id, callback)

        verify(callback).invoke(any())
    }

    @Test
    fun test_getMusicUseCase_whenFail_returnsNull() {
        val callback: (MusicDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], null) }

        given(musicRepository.getMusicFromDb(musicModel.id, callback))
            .willAnswer { callback.invoke(null) }

        getMusicUseCase(musicModel.id, callback)

        verify(callback).invoke(anyOrNull())
    }

    @Test
    fun test_getMusicsUseCase_whenSuccess_returnsListOfMusics() {
        val callback: (List<MusicDoModel>?) -> Unit = mock()
        val listOfMusics = listOf(musicModel)

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], listOfMusics) }

        given(musicRepository.getMusicsFromDb(callback))
            .willAnswer { callback.invoke(listOfMusics) }

        getMusicsUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_getMusicsUseCase_whenFail_returnsEmptyList() {
        val callback: (List<MusicDoModel>?) -> Unit = mock()
        val emptyList = listOf<MusicDoModel>()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], emptyList) }

        given(musicRepository.getMusicsFromDb(callback))
            .willAnswer { callback.invoke(emptyList) }

        getMusicsUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_saveMusicsUseCase_whenSuccess_returnsListOfInsertedMusics() = runTest {
        val callback: (List<MusicDoModel>) -> Unit = mock()
        val listOfMusics = listOf(musicModel)

        given(callback.invoke(anyOrNull())).willAnswer {
            //in real maybe this list will be a different list
            assertEquals(it.arguments[0], listOfMusics)
        }

        given(musicRepository.saveMusicsInDb(anyList(), eq(callback))).willAnswer {
            @Suppress("UNCHECKED_CAST")
            callback.invoke(it.arguments[0] as List<MusicDoModel>)
        }

        saveMusicsUseCase(listOfMusics, callback)

        verify(callback).invoke(anyList())
    }

    @Test(expected = Exception::class)
    fun test_saveMusicsUseCase_whenFail_throwsException() = runTest {
        val callback: (List<MusicDoModel>) -> Unit = mock()
        val exception = Exception("Music Was Not Saved")

        given(callback.invoke(anyList())).willAnswer { assert(false) }

        given(musicRepository.saveMusicsInDb(anyList(), eq(callback)))
            .willThrow(exception)

        saveMusicsUseCase(listOf(musicModel), callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_removeMusicUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(musicRepository.removeMusic(musicModel.id, callback))
            .willAnswer { callback.invoke() }

        removeMusicUseCase(musicModel.id, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_removeMusicUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Music Was Not Exist")

        given(callback.invoke()).willAnswer { assert(false) }

        given(musicRepository.removeMusic(musicModel.id, callback))
            .willThrow(exception)

        removeMusicUseCase(musicModel.id, callback)

        verify(callback).invoke()
    }

}