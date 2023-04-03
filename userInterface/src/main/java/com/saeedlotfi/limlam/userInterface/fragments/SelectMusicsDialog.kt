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
import com.saeedlotfi.limlam.userInterface.viewModels.SelectMusicsViewModel
import javax.inject.Inject

class SelectMusicsDialog private constructor() : BaseDialogFragment() {

    companion object {
        fun newInstance(
            listOfMusics: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>,
            selectedMusics: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>,
            except: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>,
            onDismissDialog: (selectedMusics: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>) -> Unit
        ): SelectMusicsDialog {
            return SelectMusicsDialog().also { dialog ->
                dialog.listOfMusics = listOfMusics
                dialog.selectedMusics = selectedMusics
                dialog.except = except
                dialog.onDismissDialog = onDismissDialog
            }
        }
    }

    var listOfMusics: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>? = null
    var selectedMusics: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>? = null
    var except: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>? = null
    lateinit var onDismissDialog: (selectedMusics: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>) -> Unit

    @Inject
    lateinit var userInterface: UiComponentFactory<RvListDialogUiNormal>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: MultiViewRvAdapter

    private lateinit var viewModel: SelectMusicsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getUserInterfaceComponent().inject(this)
        userInterface.createComponent(UiContext.create(requireContext()), RvListDialogUiNormal::class)
        ViewModelProvider(viewModelStore, viewModelFactory).also { vmp ->
            viewModel = vmp[SelectMusicsViewModel::class.java]
        }
        selectedMusics?.also {
            viewModel.selectedMusics = it.toMutableList()
        }
        except?.also {
            listOfMusics?.also { list ->
                viewModel.listOfMusicModels = list.toMutableList()
                except?.map { exceptItem ->
                    listOfMusics?.forEach { inputListItem ->
                        if (exceptItem.id == inputListItem.id) {
                            viewModel.listOfMusicModels.remove(inputListItem)
                            return@forEach
                        }
                    }
                }
            }
        }
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        configRecyclerView()
        saveButtonAction()
        return userInterface.view.rootView
    }

    override fun onStart() {
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        super.onStart()
    }

    private fun saveButtonAction() {
        userInterface.view.btAccept.setOnClickListener {
            onDismissDialog(viewModel.selectedMusics)
            dismiss()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun configRecyclerView() {
        viewModel.listOfMusicModels.also { musicsList ->
            userInterface.view.rvList.adapter = null
            adapter.selectedItems = viewModel.selectedMusics as MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>
            adapter.init(
                inputList = musicsList as MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>,
                mode = MultiViewRvAdapter.Mode.LINEAR_ONE_PICTURE,
                header = false,
                selectionMode = true
            ) {}
            userInterface.view.rvList.adapter = adapter
            adapter.submitList(musicsList as? List<com.saeedlotfi.limlam.domain.model.AbstractDoModel>?)
        }
    }

    override fun onDestroyView() {
        userInterface.view.rvList.adapter = null
        Runtime.getRuntime().gc()
        super.onDestroyView()
    }

}
