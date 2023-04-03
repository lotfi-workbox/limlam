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
import androidx.lifecycle.get
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.saeedlotfi.limlam.domain.model.AbstractDoModel
import com.saeedlotfi.limlam.domain.model.MusicDoModel
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
import com.saeedlotfi.limlam.userInterface.viewModels.TracksViewModel
import javax.inject.Inject

open class TracksFragment : BaseFragment(), IOnBackPressed, FilterAdapter {

    @Inject
    lateinit var userInterface: UiComponentFactory<RvListUiComponent>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: MultiViewRvAdapter

    @Inject
    lateinit var mediaPlayerCore: MediaPlayerCore

    private lateinit var viewModel: TracksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getUserInterfaceComponent().inject(this)
        userInterface.createComponent(UiContext.create(requireContext()), RvListUiComponent::class)
        viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get()
        configRecyclerView()
        return userInterface.view.rootView
    }

    override fun onStart() {
        LocalBroadcastManager.getInstance(activity as MainActivity).apply {
            registerReceiver(updateUi, IntentFilter(Constants.BC_ACTION_REFRESH_MAIN))
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
        viewModel.listOfMusics.observe(viewLifecycleOwner) {
            it?.also { musicsList ->
                musicsList.sortBy { mm -> mm.name }
                userInterface.view.rvList.adapter = null
                adapter.init(
                    inputList = musicsList as MutableList<AbstractDoModel>,
                    mode = MultiViewRvAdapter.Mode.LINEAR_ONE_PICTURE,
                    header = false,
                    selectionMode = false
                ) { position ->
                    if (adapter.currentList.firstOrNull() is MusicDoModel)
                        mediaPlayerCore.initMediaPlayer(
                            adapter.currentList.toMutableList() as MutableList<MusicDoModel>,
                            position,
                            true
                        )
                }
                if (it.isEmpty() && userInterface.view.flProgressBar.visibility != View.VISIBLE) {
                    userInterface.view.tvEmpty.visibility = View.VISIBLE
                } else if (it.isNotEmpty()) {
                    userInterface.view.flProgressBar.visibility = View.GONE
                    userInterface.view.tvEmpty.visibility = View.GONE
                }
                userInterface.view.rvList.adapter = adapter
                adapter.submitList(musicsList as? List<AbstractDoModel>?)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        if (adapter.isMainList()) return true
        adapter.backToPreviousSelection()
        return false
    }

    override fun onDestroyView() {
        viewModel.listOfMusics.removeObservers(viewLifecycleOwner)
        userInterface.view.rvList.adapter = null
        Runtime.getRuntime().gc()
        super.onDestroyView()
    }

    override fun filter(text: String?) {
        adapter.filter.filter(text)
    }

    private val updateUi: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            viewModel.refreshView()
        }
    }

}
