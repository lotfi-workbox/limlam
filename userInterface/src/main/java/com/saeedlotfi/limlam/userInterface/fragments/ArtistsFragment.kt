package com.saeedlotfi.limlam.userInterface.fragments

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
import com.saeedlotfi.limlam.userInterface._common.FilterAdapter
import com.saeedlotfi.limlam.userInterface._common.IOnBackPressed
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface._common.MultiViewRvAdapter
import com.saeedlotfi.limlam.userInterface.activities.main.MainActivity
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts.components.RvListUiComponent
import com.saeedlotfi.limlam.userInterface.viewModels.ArtistsViewModel
import javax.inject.Inject

open class ArtistsFragment : BaseFragment(), IOnBackPressed, FilterAdapter {

    @Inject
    lateinit var userInterface: UiComponentFactory<RvListUiComponent>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: MultiViewRvAdapter

    @Inject
    lateinit var mediaPlayerCore: MediaPlayerCore

    private lateinit var viewModel: ArtistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getUserInterfaceComponent().inject(this)
        userInterface.createComponent(UiContext.create(requireContext()), RvListUiComponent::class)
        ViewModelProvider(viewModelStore, viewModelFactory).also { vmp ->
            viewModel = vmp[ArtistsViewModel::class.java]
        }
        configRvAdapter()
        return userInterface.view.rootView
    }

    override fun onStart() {
        LocalBroadcastManager.getInstance(activity as MainActivity).apply {
            registerReceiver(updateUi, IntentFilter(Constants.BC_ACTION_REFRESH_ARTISTS))
        }
        super.onStart()
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(activity as MainActivity).apply {
            unregisterReceiver(updateUi)
        }
        super.onStop()
    }

    override fun onBackPressed(): Boolean {
        if (adapter.isMainList()) return true
        adapter.backToPreviousSelection()
        return false
    }

    override fun onDestroyView() {
        viewModel.listOfArtists.removeObservers(viewLifecycleOwner)
        userInterface.view.rvList.adapter = null
        Runtime.getRuntime().gc()
        super.onDestroyView()
    }

    override fun filter(text: String?) {
        adapter.filter.filter(text)
    }

    @Suppress("UNCHECKED_CAST")
    private fun configRvAdapter() {
        viewModel.listOfArtists.observe(viewLifecycleOwner) {
            it?.also { artistsList ->
                artistsList.sortBy { am -> am.name }
                userInterface.view.rvList.adapter = null
                adapter.init(
                    inputList = artistsList as MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>,
                    mode = MultiViewRvAdapter.Mode.LINEAR_ONE_PICTURE,
                    header = false,
                    selectionMode = false
                ) { position ->
                    if (adapter.currentList.firstOrNull() is com.saeedlotfi.limlam.domain.model.MusicDoModel)
                        mediaPlayerCore.initMediaPlayer(
                            adapter.currentList.toMutableList() as MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>,
                            position,
                            true
                        )
                }
                if (artistsList.isEmpty() && userInterface.view.flProgressBar.visibility != View.VISIBLE) {
                    userInterface.view.tvEmpty.visibility = View.VISIBLE
                } else if (artistsList.isNotEmpty()) {
                    userInterface.view.flProgressBar.visibility = View.GONE
                    userInterface.view.tvEmpty.visibility = View.GONE
                }
                userInterface.view.rvList.adapter = adapter
                adapter.submitList(artistsList as? MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>)
            }
        }
    }

    private val updateUi: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.refreshView()
        }
    }

}