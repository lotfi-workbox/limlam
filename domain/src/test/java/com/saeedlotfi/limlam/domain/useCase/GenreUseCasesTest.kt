package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.GenreDoModel
import com.saeedlotfi.limlam.domain.repository.GenreRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.anyList

@OptIn(ExperimentalCoroutinesApi::class)
class GenreUseCasesTest {

    private lateinit var genreRepository: GenreRepository
    private lateinit var getGenreUseCase: GetGenreUseCase
    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var saveGenresUseCase: SaveGenresUseCase
    private lateinit var removeGenreUseCase: RemoveGenreUseCase

    private val genreModel = GenreDoModel()

    @Before
    fun beforeTest() {
        genreRepository = mock()

        getGenreUseCase = GetGenreUseCase(genreRepository)
        getGenresUseCase = GetGenresUseCase(genreRepository)
        saveGenresUseCase = SaveGenresUseCase(genreRepository)
        removeGenreUseCase = RemoveGenreUseCase(genreRepository)
    }

    @Test
    fun test_getGenreUseCase_whenSuccess_returnsGenre() {
        val callback: (GenreDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], genreModel) }

        given(genreRepository.getGenreFromDb(genreModel.id, callback))
            .willAnswer { callback.invoke(genreModel) }

        getGenreUseCase(genreModel.id, callback)

        verify(callback).invoke(any())
    }

    @Test
    fun test_getGenreUseCase_whenFail_returnsNull() {
        val callback: (GenreDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], null) }

        given(genreRepository.getGenreFromDb(genreModel.id, callback))
            .willAnswer { callback.invoke(null) }

        getGenreUseCase(genreModel.id, callback)

        verify(callback).invoke(anyOrNull())
    }

    @Test
    fun test_getGenresUseCase_whenSuccess_returnsListOfGenres() {
        val callback: (List<GenreDoModel>?) -> Unit = mock()
        val listOfGenres = listOf(genreModel)

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], listOfGenres) }

        given(genreRepository.getGenresFromDb(callback))
            .willAnswer { callback.invoke(listOfGenres) }

        getGenresUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_getGenresUseCase_whenFail_returnsEmptyList() {
        val callback: (List<GenreDoModel>?) -> Unit = mock()
        val emptyList = listOf<GenreDoModel>()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], emptyList) }

        given(genreRepository.getGenresFromDb(callback))
            .willAnswer { callback.invoke(emptyList) }

        getGenresUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_saveGenresUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(genreRepository.saveGenresInDb(anyList(), eq(callback)))
            .willAnswer { callback.invoke() }

        saveGenresUseCase(listOf(genreModel), callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_saveGenresUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Genre Was Not Saved")

        given(callback.invoke()).willAnswer { assert(false) }

        given(genreRepository.saveGenresInDb(anyList(), eq(callback)))
            .willThrow(exception)

        saveGenresUseCase(listOf(genreModel), callback)

        verify(callback).invoke()
    }

    @Test
    fun test_removeGenreUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(genreRepository.removeGenre(genreModel.id, callback))
            .willAnswer { callback.invoke() }

        removeGenreUseCase(genreModel.id, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_removeGenreUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Genre Was Not Exist")

        given(callback.invoke()).willAnswer { assert(false) }

        given(genreRepository.removeGenre(genreModel.id, callback))
            .willThrow(exception)

        removeGenreUseCase(genreModel.id, callback)

        verify(callback).invoke()
    }

}