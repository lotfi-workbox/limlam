package com.saeedlotfi.limlam.userInterface.fragments.playlists

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.saeedlotfi.limlam.userInterface._common.BaseFragment
import com.saeedlotfi.limlam.userInterface._common.Constants
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.ViewModelFactory
import com.saeedlotfi.limlam.userInterface._common.*
import com.saeedlotfi.limlam.userInterface.activities.main.MainActivity
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.toast
import com.saeedlotfi.limlam.userInterface.layouts.components.RvListUiComponent
import com.saeedlotfi.limlam.userInterface.viewModels.PlayListsViewModel
import javax.inject.Inject

open class PlayListsFragment : BaseFragment(), IOnBackPressed, FilterAdapter {

    @Inject
    lateinit var userInterface: UiComponentFactory<RvListUiComponent>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: PlayListsRvAdapter

    @Inject
    lateinit var mediaPlayerCore: MediaPlayerCore

    lateinit var viewModel: PlayListsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getUserInterfaceComponent().inject(this)
        userInterface.createComponent(UiContext.create(requireContext()), RvListUiComponent::class)
        ViewModelProvider(viewModelStore, viewModelFactory).also { vmp ->
            viewModel = vmp[PlayListsViewModel::class.java]
        }
        configRecyclerView()
        return userInterface.view.rootView
    }

    override fun onStart() {
        LocalBroadcastManager.getInstance(activity as MainActivity).apply {
            registerReceiver(updateUi, IntentFilter(Constants.BC_ACTION_REFRESH_PLAYLISTS))
        }
        super.onStart()
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(activity as MainActivity).apply {
            unregisterReceiver(updateUi)
        }
        super.onStop()
    }

    @Suppress("UNCHECKED_CAST")
    private fun configRecyclerView() {
        viewModel.listOfPlayLists.observe(viewLifecycleOwner) {
            it?.also { playlistsList ->
                userInterface.view.rvList.adapter = null
                adapter.init(
                    inputList = playlistsList as MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>,
                    mode = MultiViewRvAdapter.Mode.LINEAR_FOUR_PICTURE,
                    header = false,
                    selectionMode = false
                ) { position ->
                    if (adapter.currentList.firstOrNull() is com.saeedlotfi.limlam.domain.model.MusicDoModel)
                        mediaPlayerCore.initMediaPlayer(
                            adapter.currentList.toMutableList() as MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>,
                            position,
                            true
                        )
                    else if (position == PlayListsRvAdapter.DEFAULT_ITEM_POS_CREATE_PLAYLIST) {
                        DialogsManager.showGetNameDialog(
                            parentFragmentManager,
                            "Choose name for Playlist",
                            "My Playlist ${(viewModel.listOfPlayLists.value?.size ?: 0) + 1}"
                        ) { name ->
                            DialogsManager.showSelectMusicsDialog(
                                parentFragmentManager,
                                viewModel.listOfMusics!!,
                                mutableListOf()
                            ) { list ->
                                viewModel.savePlayListsInDb(
                                    listOf(
                                        com.saeedlotfi.limlam.domain.model.PlayListDoModel(
                                            name = name,
                                            musics = list
                                        )
                                    )
                                ) {
                                    adapter.submitList(null)
                                    viewModel.refreshView()
                                    userInterface.view.rootView.toast("playlist created")
                                }
                            }
                        }
                    }
                }
                userInterface.view.rvList.adapter = adapter
                adapter.submitList(playlistsList as MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>)
            }
        }

        viewModel.favourites.observe(viewLifecycleOwner) {
            val currentItem = adapter.getCurrentlyDisplayedItem()
            adapter.updateOneItemFormParents(it)
            if (currentItem is com.saeedlotfi.limlam.domain.model.PlayListDoModel) {
                if (currentItem.id == it.id) {
                    adapter.submitList(it.subList)
                    userInterface.view.rvList.smoothScrollToPosition(0)
                }
            } else {
                viewModel.refreshView()
            }
        }

        viewModel.recentlyPlayed.observe(viewLifecycleOwner) {
            val currentItem = adapter.getCurrentlyDisplayedItem()
            adapter.updateOneItemFormParents(it)
            if (currentItem is com.saeedlotfi.limlam.domain.model.PlayListDoModel) {
                if (currentItem.id == it.id) {
                    adapter.submitList(it.subList)
                    userInterface.view.rvList.smoothScrollToPosition(0)
                }
            } else {
                viewModel.refreshView()
            }
        }
    }

    override fun filter(text: String?) {
        adapter.filter.filter(text)
    }

    override fun onBackPressed(): Boolean {
        if (adapter.isMainList()) return true
        adapter.backToPreviousSelection()
        return false
    }

    override fun onDestroyView() {
        viewModel.listOfPlayLists.removeObservers(viewLifecycleOwner)
        userInterface.view.rvList.adapter = null
        Runtime.getRuntime().gc()
        super.onDestroyView()
    }

    private val updateUi: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getStringExtra(Constants.BC_ACTION_REFRESH_PLAYLISTS)) {
                Constants.BC_KEY_REFRESH_RECENTLY_PLAYED -> {
                    viewModel.refreshRecentlyPlayed()
                }
                Constants.BC_KEY_REFRESH_FAVOURITES -> {
                    viewModel.refreshFavourites()
                }
            }
        }
    }

}


