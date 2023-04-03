package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.ArtistDoModel
import com.saeedlotfi.limlam.domain.repository.ArtistRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.anyList

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistUseCasesTest {

    private lateinit var artistRepository: ArtistRepository
    private lateinit var getArtistUseCase: GetArtistUseCase
    private lateinit var getArtistsUseCase: GetArtistsUseCase
    private lateinit var saveArtistsUseCase: SaveArtistsUseCase
    private lateinit var removeArtistUseCase: RemoveArtistUseCase

    private val artistModel = ArtistDoModel()

    @Before
    fun beforeTest() {
        artistRepository = mock()

        getArtistUseCase = GetArtistUseCase(artistRepository)
        getArtistsUseCase = GetArtistsUseCase(artistRepository)
        saveArtistsUseCase = SaveArtistsUseCase(artistRepository)
        removeArtistUseCase = RemoveArtistUseCase(artistRepository)
    }

    @Test
    fun test_getArtistUseCase_whenSuccess_returnsArtist() {
        val callback: (ArtistDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], artistModel) }

        given(artistRepository.getArtistFromDb(artistModel.id, callback))
            .willAnswer { callback.invoke(artistModel) }

        getArtistUseCase(artistModel.id, callback)

        verify(callback).invoke(any())
    }

    @Test
    fun test_getArtistUseCase_whenFail_returnsNull() {
        val callback: (ArtistDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], null) }

        given(artistRepository.getArtistFromDb(artistModel.id, callback))
            .willAnswer { callback.invoke(null) }

        getArtistUseCase(artistModel.id, callback)

        verify(callback).invoke(anyOrNull())
    }

    @Test
    fun test_getArtistsUseCase_whenSuccess_returnsListOfArtists() {
        val callback: (List<ArtistDoModel>?) -> Unit = mock()
        val listOfArtists = listOf(artistModel)

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], listOfArtists) }

        given(artistRepository.getArtistsFromDb(callback))
            .willAnswer { callback.invoke(listOfArtists) }

        getArtistsUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_getArtistsUseCase_whenFail_returnsEmptyList() {
        val callback: (List<ArtistDoModel>?) -> Unit = mock()
        val emptyList = listOf<ArtistDoModel>()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], emptyList) }

        given(artistRepository.getArtistsFromDb(callback))
            .willAnswer { callback.invoke(emptyList) }

        getArtistsUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_saveArtistsUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(artistRepository.saveArtistsInDb(anyList(), eq(callback)))
            .willAnswer { callback.invoke() }

        saveArtistsUseCase(listOf(artistModel), callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_saveArtistsUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Artist Was Not Saved")

        given(callback.invoke()).willAnswer { assert(false) }

        given(artistRepository.saveArtistsInDb(anyList(), eq(callback)))
            .willThrow(exception)

        saveArtistsUseCase(listOf(artistModel), callback)

        verify(callback).invoke()
    }

    @Test
    fun test_removeArtistUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(artistRepository.removeArtist(artistModel.id, callback))
            .willAnswer { callback.invoke() }

        removeArtistUseCase(artistModel.id, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_removeArtistUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Artist Was Not Exist")

        given(callback.invoke()).willAnswer { assert(false) }

        given(artistRepository.removeArtist(artistModel.id, callback))
            .willThrow(exception)

        removeArtistUseCase(artistModel.id, callback)

        verify(callback).invoke()
    }

}