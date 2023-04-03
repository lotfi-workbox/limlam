package com.saeedlotfi.limlam.userInterface.dependencyInjection

import com.saeedlotfi.limlam.userInterface._common.BaseActivity
import com.saeedlotfi.limlam.userInterface._common.BaseDialogFragment
import com.saeedlotfi.limlam.userInterface._common.BaseFragment
import com.saeedlotfi.limlam.userInterface.activities.PreviewerActivity
import com.saeedlotfi.limlam.userInterface.activities.main.MainActivity
import com.saeedlotfi.limlam.userInterface.dependencyInjection.module.UseCaseModule
import com.saeedlotfi.limlam.userInterface.dependencyInjection.module.UserInterfaceModule
import com.saeedlotfi.limlam.userInterface.dependencyInjection.module.UserInterfaceMultiBindingModule
import com.saeedlotfi.limlam.userInterface.dependencyInjection.module.ViewModelMultiBindingModule
import com.saeedlotfi.limlam.userInterface.fragments.*
import com.saeedlotfi.limlam.userInterface.fragments.playlists.PlayListsFragment
import com.saeedlotfi.limlam.userInterface.fragments.queue.QueueDialog
import com.saeedlotfi.limlam.userInterface.services.MusicPlayerService
import com.saeedlotfi.limlam.userInterface.services.MusicScannerService
import com.saeedlotfi.limlam.userInterface.widgets.ThreeInOneDark
import com.saeedlotfi.limlam.userInterface.widgets.ThreeInOneLight
import dagger.Subcomponent

@Subcomponent(
    modules = [
        UserInterfaceModule::class,
        UserInterfaceMultiBindingModule::class,
        ViewModelMultiBindingModule::class,
        UseCaseModule::class
    ]
)
interface UserInterfaceComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserInterfaceComponent
    }

    //services
    fun inject(service: MusicScannerService)

    fun inject(service: MusicPlayerService)

    //widgets
    fun inject(widget: ThreeInOneDark)

    fun inject(widget: ThreeInOneLight)

    //activities

    fun inject(baseActivity: BaseActivity)

    fun inject(activity: MainActivity)

    fun inject(activity: PreviewerActivity)

    //fragments

    fun inject(baseFragment: BaseFragment)

    fun inject(baseFragment: BaseDialogFragment)

    fun inject(fragment: AddToPlaylistDialog)

    fun inject(fragment: AlbumsFragment)

    fun inject(fragment: ArtistsFragment)

    fun inject(fragment: FoldersFragment)

    fun inject(fragment: GenresFragment)

    fun inject(fragment: PlayListsFragment)

    fun inject(fragment: QueueDialog)

    fun inject(fragment: SelectMusicsDialog)

    fun inject(fragment: TracksFragment)

    fun inject(fragment: AboutDialog)

    fun inject(fragment: GetStringDialog)

}