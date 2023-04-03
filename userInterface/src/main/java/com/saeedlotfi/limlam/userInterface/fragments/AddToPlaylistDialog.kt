package com.saeedlotfi.limlam.userInterface.fragments

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.saeedlotfi.limlam.userInterface._common.BaseDialogFragment
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.ViewModelFactory
import com.saeedlotfi.limlam.userInterface._common.MultiViewRvAdapter
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.layouts.RvListDialogUiNormal
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.toast
import com.saeedlotfi.limlam.userInterface.viewModels.AddToPlaylistViewModel
import javax.inject.Inject

class AddToPlaylistDialog private constructor() : BaseDialogFragment() {

    companion object {
        fun newInstance(inputMusic: com.saeedlotfi.limlam.domain.model.MusicDoModel) = AddToPlaylistDialog().also {
            it.inputMusic = inputMusic
        }
    }

    var inputMusic: com.saeedlotfi.limlam.domain.model.MusicDoModel? = null

    @Inject
    lateinit var userInterface: UiComponentFactory<RvListDialogUiNormal>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: MultiViewRvAdapter

    private lateinit var viewModel: AddToPlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getUserInterfaceComponent().inject(this)
        userInterface.createComponent(UiContext.create(requireContext()), RvListDialogUiNormal::class)
        ViewModelProvider(viewModelStore, viewModelFactory).also { vmp ->
            viewModel = vmp[AddToPlaylistViewModel::class.java]
        }
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        configRecyclerView()
        viewModel.refreshView()
        return userInterface.view.rootView
    }

    override fun onStart() {
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        super.onStart()
    }

    override fun onDestroyView() {
        viewModel.listOfPlayLists.removeObservers(viewLifecycleOwner)
        userInterface.view.rvList.adapter = null
        inputMusic = null
        Runtime.getRuntime().gc()
        super.onDestroyView()
    }

    @Suppress("UNCHECKED_CAST")
    private fun configRecyclerView() {
        viewModel.listOfPlayLists.observe(viewLifecycleOwner) {
            it?.also { musicsList ->
                userInterface.view.rvList.adapter = null
                adapter.init(
                    inputList = musicsList as MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>,
                    mode = MultiViewRvAdapter.Mode.LINEAR_FOUR_PICTURE,
                    header = false,
                    selectionMode = true
                ) { position ->
                    inputMusic?.also { mm ->
                        viewModel.listOfPlayLists.value?.get(position)?.also { pll ->
                            pll.musics?.add(mm)
                            viewModel.savePlayListsInDb(listOf(pll)) {
                                userInterface.view.rootView.toast("Music added to playlist")
                                dismiss()
                            }
                        }
                    }
                }
                userInterface.view.rvList.adapter = adapter
                adapter.submitList(musicsList as? List<com.saeedlotfi.limlam.domain.model.AbstractDoModel>?)
            }
        }
    }
}
