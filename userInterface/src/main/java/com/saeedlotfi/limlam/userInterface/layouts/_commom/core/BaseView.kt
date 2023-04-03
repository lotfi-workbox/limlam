package com.saeedlotfi.limlam.userInterface.layouts._commom.core

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams

interface BaseView<T : Context, R : View> : UiContext<T> {

    fun setLParams(view: R, lParams: LayoutParams) {
        view.layoutParams = lParams
    }

    fun getLParams(view: R): LayoutParams = view.layoutParams as LayoutParams

}