package com.saeedlotfi.limlam.userInterface.activities.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.saeedlotfi.limlam.userInterface._common.BaseActivity
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.ViewModelFactory
import com.saeedlotfi.limlam.userInterface._common.BitmapMemoryCache
import com.saeedlotfi.limlam.userInterface._common.IOnBackPressed
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore.StateChangeCallback
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore.StateChangeCallback.State
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts.mainActivityUi.MainUiNormal
import com.saeedlotfi.limlam.userInterface.services.MusicPlayerService
import com.saeedlotfi.limlam.userInterface.services.MusicScannerService
import com.saeedlotfi.limlam.userInterface.viewModels.MainViewModel
import com.saeedlotfi.limlam.userInterface.R
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var userInterface: UiComponentFactory<MainUiNormal>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mediaPlayerCore: MediaPlayerCore

    private lateinit var viewModel: MainViewModel

    private inline val bottomSheet get() = userInterface.view.cmptBottomSheet

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (userInterface.view.dlMain.isDrawerOpen(GravityCompat.START)) {
                    true -> {
                        userInterface.view.dlMain.closeDrawer(GravityCompat.START)
                    }
                    else -> {
                        when (bottomSheet.sheetBehavior.state) {
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                bottomSheet.sheetBehavior.state =
                                    BottomSheetBehavior.STATE_COLLAPSED
                            }
                            else -> {
                                when (userInterface.view.cmptSearch.rootView.isIconified) {
                                    true -> {
                                        userInterface.view.viewPager.adapter?.instantiateItem(
                                            userInterface.view.viewPager,
                                            userInterface.view.viewPager.currentItem
                                        )?.also {
                                            (it as? IOnBackPressed)?.onBackPressed()
                                                ?.also { pressed ->
                                                    if (pressed) {
                                                        doubleTabToExit()
                                                    }
                                                }
                                        }
                                    }
                                    else -> {
                                        userInterface.view.cmptSearch.rootView.isIconified = true
                                        userInterface.view.cmptSearch.rootView.isIconified = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    private val stateChangeCallback: StateChangeCallback =
        object : StateChangeCallback {
            override fun onMediaPlayerStateChange(state: State) {
                when (state) {
                    State.PLAY, State.PAUSE -> {
                        bottomSheet.cmptController.updatePlayButton(mediaPlayerCore.isPlaying())
                        bottomSheet.cmptFlat.cmptFlatController.updatePlayButton(mediaPlayerCore.isPlaying())
                        onMediaPlayerStateChange(State.SEEK)
                    }
                    State.INIT -> {
                        val musicModel = mediaPlayerCore.getCurrentPlaying()
                        bottomSheet.cmptDashboard.updateFavouritesButton(
                            musicModel?.favourite ?: false
                        )
                        bottomSheet.updateImagesAndLabels(this@MainActivity, musicModel!!)
                    }
                    State.SEEK -> {
                        if (mediaPlayerCore.isPlaying())
                            bottomSheet.cmptSeekbar.startUpdateSeekbar(
                                mediaPlayerCore.getDuration() ?: 0,
                                mediaPlayerCore.getCurrentPosition().toInt()
                            )
                        else {
                            bottomSheet.cmptSeekbar.stopUpdateSeekbar()
                            bottomSheet.cmptSeekbar.updateSeekbar(
                                mediaPlayerCore.getDuration() ?: 0,
                                mediaPlayerCore.getCurrentPosition().toInt()
                            )
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MainTheme)
        super.onCreate(savedInstanceState)
        getUserInterfaceComponent().inject(this)
        mediaPlayerCore.addStateChangeCallback(stateChangeCallback)
        userInterface.createComponent(UiContext.create(this), MainUiNormal::class)
        ViewModelProvider(viewModelStore, viewModelFactory).also { vmp ->
            viewModel = vmp[MainViewModel::class.java]
            userInterface.view.viewModel = viewModel
            userInterface.view.configViews(supportFragmentManager)
        }
        setContentView(userInterface.view.rootView)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        getStoragePermission {
            MusicPlayerService.startService(applicationContext)
            MusicScannerService.startService(applicationContext)
        }
    }

    override fun onStart() {
        super.onStart()
        mediaPlayerCore.getCurrentPlaying()?.also {
            stateChangeCallback.onMediaPlayerStateChange(State.INIT)
            stateChangeCallback.onMediaPlayerStateChange(
                if (mediaPlayerCore.isPlaying()) State.PLAY else State.PAUSE
            )
        }
    }

    override fun onDestroy() {
        bottomSheet.sheetBehavior.removeBottomSheetCallback(bottomSheet.bottomSheetCallback)
        bottomSheet.metadataExtractor.closeRetriever()
        bottomSheet.cmptSeekbar.stopUpdateSeekbar()
        mediaPlayerCore.removeStateChangeCallback(stateChangeCallback)
        BitmapMemoryCache.evictAll()
        (userInterface.view.viewPager.adapter as? MainTabAdapter)?.clear()
        ThemeManager.removeAllListeners()
        super.onDestroy()
    }

}
