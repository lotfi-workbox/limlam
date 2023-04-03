@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.saeedlotfi.limlam.userInterface._common

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.saeedlotfi.limlam.userInterface.fragments.GetStringDialog
import com.saeedlotfi.limlam.userInterface.fragments.SelectMusicsDialog

object DialogsManager {

    private fun showDialog(fm: FragmentManager, dialog: DialogFragment, tag: String): Boolean {
        fm.beginTransaction().also { ft ->
            ft.add(dialog, tag)
            ft.commitAllowingStateLoss()
        }
        return fm.findFragmentByTag(tag)?.isVisible ?: true
    }

    fun showDialogWithTag(fm: FragmentManager, dialog: DialogFragment, tag: String) {
        dismissCurrentlyShownDialog(fm)
        showDialog(fm, dialog, tag)
    }

    fun getCurrentShownDialog(fm: FragmentManager): DialogFragment? {
        fm.fragments.forEach {
            if (it.isVisible) {
                return (it as? DialogFragment)
            }
        }
        return null
    }

    fun dismissCurrentlyShownDialog(fm: FragmentManager): Boolean {
        fm.fragments.forEach {
            if (it.isVisible) {
                (it as? DialogFragment)?.dismissAllowingStateLoss()
                return true
            }
        }
        return false
    }

    fun dismissLoadingDialog(fm: FragmentManager): Boolean {
        fm.fragments.forEach {
            if (it.isVisible && it.tag == "Loading") {
                (it as? DialogFragment)?.dismissAllowingStateLoss()
                return true
            }
        }
        return false
    }

    fun dismissMessageDialog(fm: FragmentManager): Boolean {
        fm.fragments.forEach {
            if (it.isVisible && it.tag == "MessageDialog") {
                (it as? DialogFragment)?.dismissAllowingStateLoss()
                return true
            }
        }
        return false
    }

    fun showGetNameDialog(
        fm: FragmentManager,
        title: String,
        text: String?,
        onComplete: (name: String) -> Unit
    ) {
        fm.also { sfm ->
            val ft = sfm.beginTransaction()
            val prev = sfm.findFragmentByTag("GetNameDialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            GetStringDialog.title = title
            text?.also { GetStringDialog.text = it }
            GetStringDialog.onDismissDialog = onComplete
            GetStringDialog().show(ft, "GetNameDialog")
        }
    }

    fun showSelectMusicsDialog(
        fm: FragmentManager,
        inputList: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>,
        selected: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>,
        onComplete: (selected: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>) -> Unit
    ) {
        showDialogWithTag(
            fm, SelectMusicsDialog.newInstance(
                inputList.also { it.sortBy { mm -> mm.name } },
                selected,
                mutableListOf(),
                onComplete
            ), "SelectMusicsDialog"
        )
    }

}