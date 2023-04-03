package com.saeedlotfi.limlam.userInterface.dependencyInjection.factory

import android.content.Context
import com.saeedlotfi.limlam.userInterface.dependencyInjection.module.UserInterfaceMultiBindingModule
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseUiComponent
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts.bottomSheetUi.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class UiComponentFactory<R : BaseUiComponent<Context, *, *>> @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<UserInterfaceMultiBindingModule.UiComponentKey,
            Provider<BaseUiComponent<Context, *, *>>>,
    val screenSize: ScreenSize
) {

    lateinit var view: R

    fun createComponent(
        uiContext: UiContext<Context>,
        mainComponentClass: KClass<out R>
    ): R {
        val creator =
            creators.entries.firstOrNull { it.key.kClass == mainComponentClass && it.key.size == screenSize }
                ?: creators.entries.firstOrNull { it.key.kClass == mainComponentClass && it.key.size == ScreenSize.NORMAL }
                ?: throw IllegalArgumentException("unknown model class $mainComponentClass")

        @Suppress("UNCHECKED_CAST")
        view = (creator.value.get() as R)

        view.initRootView(uiContext)

        return view
    }

    enum class ScreenSize {
        SMALL,
        NORMAL,
        LARGE,
        XLARGE,
        LONG,
        LANDSCAPE,
        SPLIT_PORTRAIT,
        SPLIT_LANDSCAPE,
    }

}