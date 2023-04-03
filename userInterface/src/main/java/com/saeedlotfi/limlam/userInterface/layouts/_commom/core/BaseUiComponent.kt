package com.saeedlotfi.limlam.userInterface.layouts._commom.core

import android.content.Context
import android.view.View
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseUiComponent.Dimens

abstract class BaseUiComponent<T : Context, V, D : Dimens> where V : View, V : BaseView<T, out View> {

    abstract var rootView: V

    open fun initRootView(uiContext: UiContext<T>) {
        rootView = createView(uiContext, getDimens(uiContext))
    }

    open fun initRootView(uiContext: UiContext<T>, dimens: D?) {
        rootView = createView(uiContext, (dimens ?: getDimens(uiContext)))
    }

    abstract fun getDimens(uiContext: UiContext<T>): D

    protected abstract fun createView(uiContext: UiContext<T>, dimens: D): V

    abstract class Dimens(open var width: Int = wrapContent, open var height: Int = wrapContent){
        companion object {
            fun newInstance(width: Int, height: Int) = object : Dimens(
                width = width,
                height = height
            ){}
        }
    }

}
