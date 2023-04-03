package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.ThemeDoModel
import com.saeedlotfi.limlam.domain.repository.ThemeRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.anyList

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeUseCasesTest {

    private lateinit var themeRepository: ThemeRepository
    private lateinit var getThemeUseCase: GetThemeUseCase
    private lateinit var getThemesUseCase: GetThemesUseCase
    private lateinit var saveThemesUseCase: SaveThemesUseCase
    private lateinit var removeThemeUseCase: RemoveThemeUseCase

    private val themeModel = ThemeDoModel()

    @Before
    fun beforeTest() {
        themeRepository = mock()

        getThemeUseCase = GetThemeUseCase(themeRepository)
        getThemesUseCase = GetThemesUseCase(themeRepository)
        saveThemesUseCase = SaveThemesUseCase(themeRepository)
        removeThemeUseCase = RemoveThemeUseCase(themeRepository)
    }

    @Test
    fun test_getThemeUseCase_whenSuccess_returnsTheme() {
        val callback: (ThemeDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], themeModel) }

        given(themeRepository.getThemeFromDb(themeModel.id, callback))
            .willAnswer { callback.invoke(themeModel) }

        getThemeUseCase(themeModel.id, callback)

        verify(callback).invoke(any())
    }

    @Test
    fun test_getThemeUseCase_whenFail_returnsNull() {
        val callback: (ThemeDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], null) }

        given(themeRepository.getThemeFromDb(themeModel.id, callback))
            .willAnswer { callback.invoke(null) }

        getThemeUseCase(themeModel.id, callback)

        verify(callback).invoke(anyOrNull())
    }

    @Test
    fun test_getThemesUseCase_whenSuccess_returnsListOfThemes() {
        val callback: (List<ThemeDoModel>?) -> Unit = mock()
        val listOfThemes = listOf(themeModel)

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], listOfThemes) }

        given(themeRepository.getThemesFromDb(callback))
            .willAnswer { callback.invoke(listOfThemes) }

        getThemesUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_getThemesUseCase_whenFail_returnsEmptyList() {
        val callback: (List<ThemeDoModel>?) -> Unit = mock()
        val emptyList = listOf<ThemeDoModel>()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], emptyList) }

        given(themeRepository.getThemesFromDb(callback))
            .willAnswer { callback.invoke(emptyList) }

        getThemesUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_saveThemesUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(themeRepository.saveThemesInDb(anyList(), eq(callback)))
            .willAnswer { callback.invoke() }

        saveThemesUseCase(listOf(themeModel), callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_saveThemesUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Theme Was Not Saved")

        given(callback.invoke()).willAnswer { assert(false) }

        given(themeRepository.saveThemesInDb(anyList(), eq(callback)))
            .willThrow(exception)

        saveThemesUseCase(listOf(themeModel), callback)

        verify(callback).invoke()
    }

    @Test
    fun test_removeThemeUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(themeRepository.removeTheme(themeModel.id, callback))
            .willAnswer { callback.invoke() }

        removeThemeUseCase(themeModel.id, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_removeThemeUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Theme Was Not Exist")

        given(callback.invoke()).willAnswer { assert(false) }

        given(themeRepository.removeTheme(themeModel.id, callback))
            .willThrow(exception)

        removeThemeUseCase(themeModel.id, callback)

        verify(callback).invoke()
    }

}