package com.saeedlotfi.limlam.domain.useCase

import com.saeedlotfi.limlam.domain.model.FolderDoModel
import com.saeedlotfi.limlam.domain.repository.FolderRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.anyList

@OptIn(ExperimentalCoroutinesApi::class)
class FolderUseCasesTest {

    private lateinit var folderRepository: FolderRepository
    private lateinit var getFolderUseCase: GetFolderUseCase
    private lateinit var getFoldersUseCase: GetFoldersUseCase
    private lateinit var saveFoldersUseCase: SaveFoldersUseCase
    private lateinit var removeFolderUseCase: RemoveFolderUseCase

    private val folderModel = FolderDoModel()

    @Before
    fun beforeTest() {
        folderRepository = mock()

        getFolderUseCase = GetFolderUseCase(folderRepository)
        getFoldersUseCase = GetFoldersUseCase(folderRepository)
        saveFoldersUseCase = SaveFoldersUseCase(folderRepository)
        removeFolderUseCase = RemoveFolderUseCase(folderRepository)
    }

    @Test
    fun test_getFolderUseCase_whenSuccess_returnsFolder() {
        val callback: (FolderDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], folderModel) }

        given(folderRepository.getFolderFromDb(folderModel.id, callback))
            .willAnswer { callback.invoke(folderModel) }

        getFolderUseCase(folderModel.id, callback)

        verify(callback).invoke(any())
    }

    @Test
    fun test_getFolderUseCase_whenFail_returnsNull() {
        val callback: (FolderDoModel?) -> Unit = mock()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], null) }

        given(folderRepository.getFolderFromDb(folderModel.id, callback))
            .willAnswer { callback.invoke(null) }

        getFolderUseCase(folderModel.id, callback)

        verify(callback).invoke(anyOrNull())
    }

    @Test
    fun test_getFoldersUseCase_whenSuccess_returnsListOfFolders() {
        val callback: (List<FolderDoModel>?) -> Unit = mock()
        val listOfFolders = listOf(folderModel)

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], listOfFolders) }

        given(folderRepository.getFoldersFromDb(callback))
            .willAnswer { callback.invoke(listOfFolders) }

        getFoldersUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_getFoldersUseCase_whenFail_returnsEmptyList() {
        val callback: (List<FolderDoModel>?) -> Unit = mock()
        val emptyList = listOf<FolderDoModel>()

        given(callback.invoke(any()))
            .willAnswer { assertEquals(it.arguments[0], emptyList) }

        given(folderRepository.getFoldersFromDb(callback))
            .willAnswer { callback.invoke(emptyList) }

        getFoldersUseCase(callback)

        verify(callback).invoke(anyList())
    }

    @Test
    fun test_saveFoldersUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(folderRepository.saveFoldersInDb(anyList(), eq(callback)))
            .willAnswer { callback.invoke() }

        saveFoldersUseCase(listOf(folderModel), callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_saveFoldersUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Folder Was Not Saved")

        given(callback.invoke()).willAnswer { assert(false) }

        given(folderRepository.saveFoldersInDb(anyList(), eq(callback)))
            .willThrow(exception)

        saveFoldersUseCase(listOf(folderModel), callback)

        verify(callback).invoke()
    }

    @Test
    fun test_removeFolderUseCase_whenSuccess_returnsUnit() = runTest {
        val callback: () -> Unit = mock()

        given(folderRepository.removeFolder(folderModel.id, callback))
            .willAnswer { callback.invoke() }

        removeFolderUseCase(folderModel.id, callback)

        verify(callback).invoke()
    }

    @Test(expected = Exception::class)
    fun test_removeFolderUseCase_whenFail_throwsException() = runTest {
        val callback: () -> Unit = mock()
        val exception = Exception("Folder Was Not Exist")

        given(callback.invoke()).willAnswer { assert(false) }

        given(folderRepository.removeFolder(folderModel.id, callback))
            .willThrow(exception)

        removeFolderUseCase(folderModel.id, callback)

        verify(callback).invoke()
    }

}