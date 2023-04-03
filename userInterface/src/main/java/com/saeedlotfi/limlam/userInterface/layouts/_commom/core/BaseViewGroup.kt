package com.saeedlotfi.limlam.userInterface.layouts._commom.core

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams

interface BaseViewGroup<T : Context, R : ViewGroup> : BaseView<T, R> {

    class LParams(width: Int, height: Int) : LayoutParams(width, height) {
        companion object {
            const val MATCH_PARENT = -1
            const val WRAP_CONTENT = -2
        }
    }

}