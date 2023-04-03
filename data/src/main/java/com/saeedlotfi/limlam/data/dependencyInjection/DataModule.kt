package com.saeedlotfi.limlam.data.dependencyInjection

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.repository.*
import com.saeedlotfi.limlam.domain.repository.*
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun realmManager(): RealmManager = RealmManager()

    @Provides
    fun commonRepository(
        realmManager: RealmManager
    ): CommonRepository = CommonRepositoryImpl(realmManager)

    @Provides
    fun musicRepository(
        realmManager: RealmManager
    ): MusicRepository = MusicRepositoryImpl(realmManager)

    @Provides
    fun albumRepository(
        realmManager: RealmManager
    ): AlbumRepository = AlbumRepositoryImpl(realmManager)

    @Provides
    fun artistRepository(
        realmManager: RealmManager
    ): ArtistRepository = ArtistRepositoryImpl(realmManager)

    @Provides
    fun folderRepository(
        realmManager: RealmManager
    ): FolderRepository = FolderRepositoryImpl(realmManager)

    @Provides
    fun genreRepository(
        realmManager: RealmManager
    ): GenreRepository = GenreRepositoryImpl(realmManager)

    @Provides
    fun playlistRepository(
        realmManager: RealmManager
    ): PlayListRepository = PlayListRepositoryImpl(realmManager)

    @Provides
    fun themeRepository(
        realmManager: RealmManager
    ): ThemeRepository = ThemeRepositoryImpl(realmManager)

}