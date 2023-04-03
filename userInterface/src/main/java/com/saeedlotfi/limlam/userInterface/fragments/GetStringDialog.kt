package com.saeedlotfi.limlam.userInterface.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.saeedlotfi.limlam.userInterface._common.BaseDialogFragment
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface._common.toEditable
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.layouts.GetNameDialogUiNormal
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import javax.inject.Inject

class GetStringDialog : BaseDialogFragment() {

    companion object {
        var title : String = "Choose name for Playlist"
        var text : String = ""
        lateinit var onDismissDialog: (name: String) -> Unit
    }

    @Inject
    lateinit var userInterface: UiComponentFactory<GetNameDialogUiNormal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getUserInterfaceComponent().inject(this)
        userInterface.createComponent(UiContext.create(requireContext()), GetNameDialogUiNormal::class)
        userInterface.view.tvTitle.text = title
        userInterface.view.etName.text = text.toEditable()
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        showKeyBoard()
        cancelButtonAction()
        okButtonAction()
        return userInterface.view.rootView
    }

    override fun onStart() {
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        super.onStart()
    }

    private fun okButtonAction() {
       userInterface.view.btOk.setOnClickListener {
            hideKeyBoard()
            dismissDialog {
                userInterface.view.etName.also { editText ->
                    onDismissDialog(
                        editText.text.toString()
                    )
                }
            }
        }
    }

    private fun cancelButtonAction() {
        userInterface.view.btCancel.setOnClickListener {
            hideKeyBoard()
            dismissDialog {}
        }
    }

    private fun showKeyBoard() {
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also { imm ->
            @Suppress("DEPRECATION")
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    private fun hideKeyBoard() {
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also { imm ->
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun dismissDialog(onComplete: () -> Unit) {
        object : CountDownTimer(250, 250) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                dismiss()
                onComplete()
            }
        }.start()
    }

    override fun onDestroyView() {
        Runtime.getRuntime().gc()
        super.onDestroyView()
    }
    
}
