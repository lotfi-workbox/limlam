package com.saeedlotfi.limlam.userInterface.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.saeedlotfi.limlam.domain.model.*
import com.saeedlotfi.limlam.domain.useCase.*
import com.saeedlotfi.limlam.userInterface._common.*
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class MusicScannerService : Service() {

    companion object {

        fun startService(context: Context) {
            if (!context.isServiceRunning(MusicScannerService::class.java)) {
                Intent(context, MusicScannerService::class.java).also { intent ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent)
                    } else {
                        context.startService(intent)
                    }
                }
            }
        }

    }

    @Inject
    lateinit var saveMusicsUseCase: SaveMusicsUseCase

    @Inject
    lateinit var saveArtistsUseCase: SaveArtistsUseCase

    @Inject
    lateinit var saveAlbumsUseCase: SaveAlbumsUseCase

    @Inject
    lateinit var saveGenresUseCase: SaveGenresUseCase

    @Inject
    lateinit var saveFoldersUseCase: SaveFoldersUseCase

    @Inject
    lateinit var metadataExtractor: MusicMetadataExtractor

    private var serviceJob = SupervisorJob()

    private val uiScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val jobList = mutableListOf<Job>()

    private var mainTaskCompleted: Boolean = false

    private val findAndAddMusics = DeepRecursiveFunction<List<String>, Unit> { paths ->
        val audioFiles: ArrayList<MusicDoModel> = arrayListOf()
        paths.forEach { path ->
            File(path).listFiles()?.forEachIndexed { _, it ->
                if (it.path != externalCacheDir?.parentFile?.parentFile?.parent) {
                    if (it.isDirectory && !it.isHidden) {
                        callRecursive(listOf(path + "/${it.name}"))
                    } else if (it.isFile && !it.isHidden) {
                        when (val fileType = it.getType()) {
                            ".mp3", ".aac", ".wav", ".wma", ".m4a" -> {
                                metadataExtractor.extractPicture(it.path) { bmp ->
                                    if (bmp != null)
                                        BitmapDiskCache.putImage(it.path.hashCode(), bmp)
                                }
                                metadataExtractor.extractData(it.path) { title, artist, album, genre, duration ->
                                    audioFiles.add(
                                        MusicDoModel(
                                            name = it.name.substring(0..(it.name.length - (fileType.length + 1))),
                                            type = fileType,
                                            path = it.path,
                                            parent = it.parentFile?.path,
                                            parentName = it.parentFile?.name,
                                            title = title,
                                            artist = artist,
                                            album = album,
                                            genre = genre,
                                            duration = duration
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (audioFiles.isNotEmpty()) {
            val saveToDataBaseProgress: Job = uiScope.launch {
                saveMusicsUseCase(audioFiles) { newMusics ->
                    if (newMusics.isNotEmpty()) {
                        sendBroadCast(Constants.BC_ACTION_REFRESH_MAIN)
                        uiScope.launch {
                            saveFoldersUseCase(
                                listOf(
                                    FolderDoModel(
                                        name = newMusics.first().parentName,
                                        path = newMusics.first().parent,
                                        musics = newMusics
                                    )
                                )
                            ) {
                                sendBroadCast(Constants.BC_ACTION_REFRESH_FOLDERS)
                            }
                            saveGenresUseCase(newMusics.extractGenres()) {
                                sendBroadCast(Constants.BC_ACTION_REFRESH_GENRES)
                            }
                            saveAlbumsUseCase(newMusics.extractAlbums()) {
                                sendBroadCast(Constants.BC_ACTION_REFRESH_ALBUMS)
                            }
                            saveArtistsUseCase(newMusics.extractArtists()) {
                                sendBroadCast(Constants.BC_ACTION_REFRESH_ARTISTS)
                            }
                        }
                    }
                }
            }
            saveToDataBaseProgress.invokeOnCompletion {
                uiScope.launch {
                    delay(1000)
                    jobList.remove(saveToDataBaseProgress)
                    if (mainTaskCompleted && jobList.isEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            stopForeground(STOP_FOREGROUND_REMOVE)
                        } else {
                            @Suppress("DEPRECATION")
                            stopForeground(true)
                        }
                        stopSelf()
                    }
                }
            }
            jobList.add(saveToDataBaseProgress)
            saveToDataBaseProgress.start()
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(
            this@MusicScannerService::class.hashCode(),
            NotificationCenter.getProgressNotification(
                this,
                "File Scanner Progress",
                "Scanning Files"
            )
        )
        getUserInterfaceComponent().inject(this)
        metadataExtractor.initRetriever()
        uiScope.launch {
            withContext(Dispatchers.IO) {
                findAndAddMusics(getStoragePaths()) {
                    mainTaskCompleted = true
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun List<MusicDoModel>.extractGenres(): List<GenreDoModel> {
        val genres: MutableList<GenreDoModel> = mutableListOf()
        forEach { mm ->
            if (genres.firstOrNull { it.name == mm.genre }?.also { gm ->
                    gm.musics?.add(mm)
                } == null) {
                genres.add(GenreDoModel(mm.genre, mutableListOf(mm)))
            }
        }
        return genres
    }

    private fun List<MusicDoModel>.extractAlbums(): List<AlbumDoModel> {
        val albums: MutableList<AlbumDoModel> = mutableListOf()
        forEach { mm ->
            if (albums.firstOrNull { it.name == mm.album }?.also { am ->
                    am.musics?.add(mm)
                } == null) {
                albums.add(
                    AlbumDoModel(
                        mm.album,
                        mm.artist,
                        mutableListOf(mm)
                    )
                )
            }

        }
        return albums
    }

    private fun List<MusicDoModel>.extractArtists(): List<ArtistDoModel> {
        val artists: MutableList<ArtistDoModel> = mutableListOf()
        extractAlbums().forEach { am ->
            if (artists.firstOrNull { it.name == am.artist }?.also { arm ->
                    arm.albums?.add(am)
                } == null) {
                artists.add(
                    ArtistDoModel(
                        am.artist,
                        mutableListOf(am)
                    )
                )
            }
        }
        return artists
    }

    override fun onDestroy() {
        metadataExtractor.closeRetriever()
        serviceJob.cancel()
        super.onDestroy()
    }

}
