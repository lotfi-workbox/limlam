package com.saeedlotfi.limlam.userInterface.fragments

import android.os.Bundle
import android.view.*
import com.saeedlotfi.limlam.userInterface._common.BaseDialogFragment
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.layouts.AboutUiNormal
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import javax.inject.Inject

class AboutDialog : BaseDialogFragment() {

    @Inject
    lateinit var userInterface: UiComponentFactory<AboutUiNormal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getUserInterfaceComponent().inject(this)
        userInterface.createComponent(UiContext.create(requireContext()), AboutUiNormal::class)
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        return userInterface.view.rootView
    }

    override fun onStart() {
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        super.onStart()
    }

    override fun onDestroyView() {
        Runtime.getRuntime().gc()
        super.onDestroyView()
    }
}