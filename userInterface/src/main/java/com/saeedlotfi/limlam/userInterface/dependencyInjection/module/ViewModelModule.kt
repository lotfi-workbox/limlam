@file:Suppress("unused")

package com.saeedlotfi.limlam.userInterface.dependencyInjection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.ViewModelFactory
import com.saeedlotfi.limlam.userInterface.viewModels.*
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelMultiBindingModule {

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    annotation class ViewModelKey(val value : KClass<out ViewModel>)

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PreviewerViewModel::class)
    abstract fun previewerViewModel(viewModel: PreviewerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddToPlaylistViewModel::class)
    abstract fun addToPlaylistViewModel(viewModel: AddToPlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumsViewModel::class)
    abstract fun albumsViewModel(viewModel: AlbumsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArtistsViewModel::class)
    abstract fun artistsViewModel(viewModel: ArtistsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FoldersViewModel::class)
    abstract fun foldersViewModel(viewModel: FoldersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GenresViewModel::class)
    abstract fun genresViewModel(viewModel: GenresViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayListsViewModel::class)
    abstract fun playListsViewModel(viewModel: PlayListsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectMusicsViewModel::class)
    abstract fun selectMusicsViewModel(viewModel: SelectMusicsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TracksViewModel::class)
    abstract fun tracksViewModel(viewModel: TracksViewModel): ViewModel

}