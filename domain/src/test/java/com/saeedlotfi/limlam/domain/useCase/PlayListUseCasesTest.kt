package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.model.PlayListDoModel
import com.saeedlotfi.limlam.domain.repository.PlayListRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.anyList

@OptIn(ExperimentalCoroutinesApi::class)
class PlayListUseCasesTest {

    private lateinit var playListRepository: PlayListRepository
    private lateinit var getPlayListUseCase: GetPlayListUseCase
    private lateinit var getPlayListsUseCase: GetPlayListsUseCase
    private lateinit var savePlayListsUseCase: SavePlayListsUseCase
    private lateinit var removePlayListUseCase: RemovePlayListUseCase
    private lateinit var addToRecentlyPlayedUseCase: AddToRecentlyPlayedUseCase
    private lateinit var addOrRemoveFromFavouritesUseCase: AddOrRemoveFromFavouritesUseCase

    private val playListModel = PlayListDoModel()
    private val musicModel = MusicDoModel()

    @Before
    fun beforeTest() {
        playListRepository = mock()

        getPlayListUseCase = GetPlayListUseCase(playListRepository)
        getPlayListsUseCase = GetPlayListsUseCase(playListRepository)
        savePlayListsUseCase = SavePlayListsUseCase(playListRepository)
        removePlayListUseCase = RemovePlayListUseCase(playListRepository)
        addToRecentlyPlayedUseCase = AddToRecentlyPlayedUseCase(playListRepository)
        addOrRemoveFromFavouritesUseCase = AddOrRemoveFromFavouritesUseCase(playListRepository)
    }

    @Test
    fun test_getPlayListUseCase_whenSuccess_returnsPlayList() {
        val callback: (PlayListDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], playListModel) }

        given(playListRepository.getPlayListFromDb(playListModel.id, callback))
            .willAnswer { callback.invoke(playListModel) }

        getPlayListUseCase(playListModel.id, callback)

        verify(callback).invoke(any())
    }

    @Test
    fun test_getPlayListUseCase_whenFail_returnsNull() {
        val callback: (PlayListDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], null) }

        given(playListRepository.getPlayListFromDb(playListModel.id, callback))
            .willAnswer { callback.invoke(null) }

        getPlayListUseCase(playListModel.id, callback)

        verify(callback).invoke(anyOrNull())
    }

    @Test
    fun test_getPlayListsUseCase_whenSuccess_returnsListOfPlayLists() {
        val callback: (List<PlayListDoModel>?) -> Unit = mock()
        val listOfPlayLists = listOf(playListModel)

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], listOfPlayLists) }

        given(playListRepository.getPlayListsFromDb(callback))
            .willAnswer { callback.invoke(listOfPlayLists) }

        getPlayListsUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_getPlayListsUseCase_whenFail_returnsEmptyList() {
        val callback: (List<PlayListDoModel>?) -> Unit = mock()
        val emptyList = listOf<PlayListDoModel>()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], emptyList) }

        given(playListRepository.getPlayListsFromDb(callback))
            .willAnswer { callback.invoke(emptyList) }

        getPlayListsUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_savePlayListsUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(playListRepository.savePlayListsInDb(anyList(), eq(callback)))
            .willAnswer { callback.invoke() }

        savePlayListsUseCase(listOf(playListModel), callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_savePlayListsUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("PlayList Was Not Saved")

        given(callback.invoke()).willAnswer { assert(false) }

        given(playListRepository.savePlayListsInDb(anyList(), eq(callback)))
            .willThrow(exception)

        savePlayListsUseCase(listOf(playListModel), callback)

        verify(callback).invoke()
    }

    @Test
    fun test_removePlayListUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(playListRepository.removePlayList(playListModel.id, callback))
            .willAnswer { callback.invoke() }

        removePlayListUseCase(playListModel.id, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_removePlayListUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("PlayList Was Not Exist")

        given(callback.invoke()).willAnswer { assert(false) }

        given(playListRepository.removePlayList(playListModel.id, callback))
            .willThrow(exception)

        removePlayListUseCase(playListModel.id, callback)

        verify(callback).invoke()
    }

    @Test
    fun test_addToRecentlyPlayedUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(playListRepository.addToRecentlyPlayed(any(), eq(callback)))
            .willAnswer { callback.invoke() }

        addToRecentlyPlayedUseCase(musicModel, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_addToRecentlyPlayedUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Music Was Not Added To RecentlyPlayed")

        given(callback.invoke()).willAnswer { assert(false) }

        given(playListRepository.addToRecentlyPlayed(any(), eq(callback)))
            .willThrow(exception)

        addToRecentlyPlayedUseCase(musicModel, callback)

        verify(callback).invoke()
    }

    @Test
    fun test_addOrRemoveFromFavouritesUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(playListRepository.addOrRemoveFromFavourites(any(), eq(callback)))
            .willAnswer { callback.invoke() }

        addOrRemoveFromFavouritesUseCase(musicModel, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_addOrRemoveFromFavouritesUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Music Was Not Added To Favourites")

        given(callback.invoke()).willAnswer { assert(false) }

        given(playListRepository.addOrRemoveFromFavourites(any(), eq(callback)))
            .willThrow(exception)

        addOrRemoveFromFavouritesUseCase(musicModel, callback)

        verify(callback).invoke()
    }

}