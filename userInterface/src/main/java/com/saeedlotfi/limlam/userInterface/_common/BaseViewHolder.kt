package com.saeedlotfi.limlam.userInterface._common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts.components.BaseRvItemUiComponent

abstract class BaseViewHolder(open val view: BaseRvItemUiComponent<*, *>) :
    RecyclerView.ViewHolder(view.rootView) {

    abstract fun bind(
        item: com.saeedlotfi.limlam.domain.model.AbstractDoModel,
        onItemClickListener: (click: Boolean) -> Unit
    )

    companion object {
        inline fun <reified T : BaseViewHolder, reified V : BaseRvItemUiComponent<*, *>> from(
            userInterface: UiComponentFactory<BaseRvItemUiComponent<*, *>>, parent: ViewGroup,
        ): T {
            return T::class.java.getDeclaredConstructor(V::class.java).newInstance(
                userInterface.createComponent(UiContext.create(parent.context), V::class)
            )
        }
    }

}