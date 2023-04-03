package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.AlbumDoModel
import com.saeedlotfi.limlam.domain.repository.AlbumRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.anyList

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumUseCasesTest {

    private lateinit var albumRepository: AlbumRepository
    private lateinit var getAlbumUseCase: GetAlbumUseCase
    private lateinit var getAlbumsUseCase: GetAlbumsUseCase
    private lateinit var saveAlbumsUseCase: SaveAlbumsUseCase
    private lateinit var removeAlbumUseCase: RemoveAlbumUseCase

    private val albumModel = AlbumDoModel()

    @Before
    fun beforeTest() {
        albumRepository = mock()

        getAlbumUseCase = GetAlbumUseCase(albumRepository)
        getAlbumsUseCase = GetAlbumsUseCase(albumRepository)
        saveAlbumsUseCase = SaveAlbumsUseCase(albumRepository)
        removeAlbumUseCase = RemoveAlbumUseCase(albumRepository)
    }

    @Test
    fun test_getAlbumUseCase_whenSuccess_returnsAlbum() {
        val callback: (AlbumDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], albumModel) }

        given(albumRepository.getAlbumFromDb(albumModel.id, callback))
            .willAnswer { callback.invoke(albumModel) }

        getAlbumUseCase(albumModel.id, callback)

        verify(callback).invoke(any())
    }

    @Test
    fun test_getAlbumUseCase_whenFail_returnsNull() {
        val callback: (AlbumDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], null) }

        given(albumRepository.getAlbumFromDb(albumModel.id, callback))
            .willAnswer { callback.invoke(null) }

        getAlbumUseCase(albumModel.id, callback)

        verify(callback).invoke(anyOrNull())
    }

    @Test
    fun test_getAlbumsUseCase_whenSuccess_returnsListOfAlbums() {
        val callback: (List<AlbumDoModel>?) -> Unit = mock()
        val listOfAlbums = listOf(albumModel)

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], listOfAlbums) }

        given(albumRepository.getAlbumsFromDb(callback))
            .willAnswer { callback.invoke(listOfAlbums) }

        getAlbumsUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_getAlbumsUseCase_whenFail_returnsEmptyList() {
        val callback: (List<AlbumDoModel>?) -> Unit = mock()
        val emptyList = listOf<AlbumDoModel>()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], emptyList) }

        given(albumRepository.getAlbumsFromDb(callback))
            .willAnswer { callback.invoke(emptyList) }

        getAlbumsUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_saveAlbumsUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(albumRepository.saveAlbumsInDb(anyList(), eq(callback)))
            .willAnswer { callback.invoke() }

        saveAlbumsUseCase(listOf(albumModel), callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_saveAlbumsUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Album Was Not Saved")

        given(callback.invoke()).willAnswer { assert(false) }

        given(albumRepository.saveAlbumsInDb(anyList(), eq(callback)))
            .willThrow(exception)

        saveAlbumsUseCase(listOf(albumModel), callback)

        verify(callback).invoke()
    }

    @Test
    fun test_removeAlbumUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(albumRepository.removeAlbum(albumModel.id, callback))
            .willAnswer { callback.invoke() }

        removeAlbumUseCase(albumModel.id, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_removeAlbumUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Album Was Not Exist")

        given(callback.invoke()).willAnswer { assert(false) }

        given(albumRepository.removeAlbum(albumModel.id, callback))
            .willThrow(exception)

        removeAlbumUseCase(albumModel.id, callback)

        verify(callback).invoke()
    }

}