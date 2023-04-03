package com.saeedlotfi.limlam.userInterface.layouts.components

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseUiComponent
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseUiComponent.Dimens
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseView

abstract class BaseRvItemUiComponent<V, D : Dimens> :
    BaseUiComponent<Context, V, D>() where V : View, V : BaseView<Context, out View> {

    abstract override var rootView: V

    abstract var mainLayout: ViewGroup

    var selected: Boolean = false

    fun select() {
        if (!selected) {
            mainLayout.post {
                mainLayout.setBackgroundColor(
                    Color.parseColor(ThemeManager.theme.selectionBackgrounds)
                )
            }
            selected = true
        }
    }

    fun unselect() {
        if (selected) {
            mainLayout.post {
                mainLayout.setBackgroundColor(
                    Color.parseColor(ThemeManager.theme.backgrounds)
                )
            }
            selected = false
        }
    }
}


