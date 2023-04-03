package com.saeedlotfi.limlam.userInterface.dependencyInjection.module

import com.saeedlotfi.limlam.domain.repository.*
import com.saeedlotfi.limlam.domain.useCase.*
import dagger.Module
import dagger.Provides

@Module
internal class UseCaseModule {

    //region AlbumRepository

    @Provides
    fun getAlbumsUseCase(
        albumRepository: AlbumRepository
    ) = GetAlbumsUseCase(albumRepository)

    @Provides
    fun getAlbumUseCase(
        albumRepository: AlbumRepository
    ) = GetAlbumUseCase(albumRepository)

    @Provides
    fun saveAlbumUseCase(
        albumRepository: AlbumRepository
    ) = SaveAlbumsUseCase(albumRepository)

    @Provides
    fun removeAlbumUseCase(
        albumRepository: AlbumRepository
    ) = RemoveAlbumUseCase(albumRepository)

    //endregion AlbumRepository

    //region ArtistRepository

    @Provides
    fun getArtistsUseCase(
        artistRepository: ArtistRepository
    ) = GetArtistsUseCase(artistRepository)

    @Provides
    fun getArtistUseCase(
        artistRepository: ArtistRepository
    ) = GetArtistUseCase(artistRepository)

    @Provides
    fun saveArtistUseCase(
        artistRepository: ArtistRepository
    ) = SaveArtistsUseCase(artistRepository)

    @Provides
    fun removeArtistUseCase(
        artistRepository: ArtistRepository
    ) = RemoveArtistUseCase(artistRepository)

    //endregion ArtistRepository

    //region CommonRepository

    @Provides
    fun getStateUseCase(
        commonRepository: CommonRepository
    ) = GetStateUseCase(commonRepository)

    @Provides
    fun saveStateUseCase(
        commonRepository: CommonRepository
    ) = SaveStateUseCase(commonRepository)

    //endregion CommonRepository

    //region FolderRepository

    @Provides
    fun getFoldersUseCase(
        folderRepository: FolderRepository
    ) = GetFoldersUseCase(folderRepository)

    @Provides
    fun getFolderUseCase(
        folderRepository: FolderRepository
    ) = GetFolderUseCase(folderRepository)

    @Provides
    fun saveFoldersUseCase(
        folderRepository: FolderRepository
    ) = SaveFoldersUseCase(folderRepository)

    @Provides
    fun removeFolderUseCase(
        folderRepository: FolderRepository
    ) = RemoveFolderUseCase(folderRepository)

    //endregion FolderRepository

    //region GenreRepository

    @Provides
    fun getGenresUseCase(
        genreRepository: GenreRepository
    ) = GetGenresUseCase(genreRepository)

    @Provides
    fun getGenreUseCase(
        genreRepository: GenreRepository
    ) = GetGenreUseCase(genreRepository)

    @Provides
    fun saveGenresUseCase(
        genreRepository: GenreRepository
    ) = SaveGenresUseCase(genreRepository)

    @Provides
    fun removeGenreUseCase(
        genreRepository: GenreRepository
    ) = RemoveGenreUseCase(genreRepository)

    //endregion GenreRepository

    //region MusicRepository

    @Provides
    fun getMusicsUseCase(
        musicRepository: MusicRepository
    ) = GetMusicsUseCase(musicRepository)

    @Provides
    fun getMusicUseCase(
        musicRepository: MusicRepository
    ) = GetMusicUseCase(musicRepository)

    @Provides
    fun saveMusicsUseCase(
        musicRepository: MusicRepository
    ) = SaveMusicsUseCase(musicRepository)

    @Provides
    fun removeMusicUseCase(
        musicRepository: MusicRepository
    ) = RemoveMusicUseCase(musicRepository)

    //endregion MusicRepository

    //region PlayListRepository

    @Provides
    fun getPlayListsUseCase(
        playListRepository: PlayListRepository
    ) = GetPlayListsUseCase(playListRepository)

    @Provides
    fun getPlayListUseCase(
        playListRepository: PlayListRepository
    ) = GetPlayListUseCase(playListRepository)

    @Provides
    fun savePlayListsUseCase(
        playListRepository: PlayListRepository
    ) = SavePlayListsUseCase(playListRepository)

    @Provides
    fun removePlayListUseCase(
        playListRepository: PlayListRepository
    ) = RemovePlayListUseCase(playListRepository)

    @Provides
    fun addToRecentlyPlayedUseCase(
        playListRepository: PlayListRepository
    ) = AddToRecentlyPlayedUseCase(playListRepository)

    @Provides
    fun addOrRemoveFromFavouritesUseCase(
        playListRepository: PlayListRepository
    ) = AddOrRemoveFromFavouritesUseCase(playListRepository)

    //endregion PlayListRepository

    //region ThemeRepository

    @Provides
    fun getThemesUseCase(
        themeRepository: ThemeRepository
    ) = GetThemesUseCase(themeRepository)

    @Provides
    fun getThemeUseCase(
        themeRepository: ThemeRepository
    ) = GetThemeUseCase(themeRepository)

    @Provides
    fun saveThemesUseCase(
        themeRepository: ThemeRepository
    ) = SaveThemesUseCase(themeRepository)

    @Provides
    fun removeThemeUseCase(
        themeRepository: ThemeRepository
    ) = RemoveThemeUseCase(themeRepository)

    //endregion ThemeRepository

}