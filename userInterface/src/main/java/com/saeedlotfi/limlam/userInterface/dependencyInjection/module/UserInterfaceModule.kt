@file:Suppress("unused")

package com.saeedlotfi.limlam.userInterface.dependencyInjection.module

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.layouts.AboutUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.GetNameDialogUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.PreviewerUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.RvListDialogUiNormal
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.bottomSheetUi.*
import com.saeedlotfi.limlam.userInterface.layouts.components.RvListUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.mainActivityUi.MainUiLandscape
import com.saeedlotfi.limlam.userInterface.layouts.mainActivityUi.MainUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.mainActivityUi.MainUiXSplitPL
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.*
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
class UserInterfaceModule {

    @Provides
    fun getScreenSizes(application: Application): UiComponentFactory.ScreenSize {

        val ui = application.applicationContext.resources

        val density = (ui.displayMetrics.widthPixels / ui.displayMetrics.density).toInt()
        val aspectRatio =
            (ui.displayMetrics.heightPixels * 1f) / (ui.displayMetrics.widthPixels * 1f)

        return when (ui.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                when (density) {
                    //small screen
                    in 0..349 ->
                        if (aspectRatio in 1f..1.4f) UiComponentFactory.ScreenSize.SPLIT_PORTRAIT
                        else UiComponentFactory.ScreenSize.SMALL
                    //normal screen
                    in 350..599 ->
                        when (aspectRatio) {
                            in 1f..1.4f -> UiComponentFactory.ScreenSize.SPLIT_PORTRAIT
                            in 1.9f..3f -> UiComponentFactory.ScreenSize.LONG
                            else -> UiComponentFactory.ScreenSize.NORMAL
                        }
                    //large screen
                    in 600..900 -> UiComponentFactory.ScreenSize.LARGE
                    //xlarge screen
                    else ->
                        if (aspectRatio !in 1f..1.4f) UiComponentFactory.ScreenSize.LARGE
                        else UiComponentFactory.ScreenSize.XLARGE
                }
            }
            else -> {
                when (density) {
                    in 0..500 -> UiComponentFactory.ScreenSize.SPLIT_LANDSCAPE
                    else -> UiComponentFactory.ScreenSize.LANDSCAPE
                }
            }
        }.also {
            Log.i("screenSize", "density : $density")
            Log.i("screenSize", "aspectRatio : $aspectRatio")
            Log.i("screenSize", "orientation : ${ui.configuration.orientation}")
            Log.i("screenSize", it.name)
        }
    }

}

@Module
abstract class UserInterfaceMultiBindingModule {

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey(unwrapValue = false)
    annotation class UiComponentKey(
        val kClass: KClass<out BaseUiComponent<Context, *, *>>,
        val size: UiComponentFactory.ScreenSize
    )

    @Binds
    abstract fun uiComponentFactory(factory: UiComponentFactory<*>): UiComponentFactory<*>

    //region MainUi
    @Binds
    @IntoMap
    @UiComponentKey(MainUiNormal::class, UiComponentFactory.ScreenSize.SMALL)
    abstract fun mainUiSmall(userInterface: MainUiNormal): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(MainUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun mainUiNormal(userInterface: MainUiNormal): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(MainUiNormal::class, UiComponentFactory.ScreenSize.LANDSCAPE)
    abstract fun mainUiLandscape(userInterface: MainUiLandscape): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(MainUiNormal::class, UiComponentFactory.ScreenSize.SPLIT_PORTRAIT)
    abstract fun mainUiSplitP(userInterface: MainUiXSplitPL): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(MainUiNormal::class, UiComponentFactory.ScreenSize.SPLIT_LANDSCAPE)
    abstract fun mainUiSplitL(userInterface: MainUiXSplitPL): BaseUiComponent<Context, *, *>


    //endregion MainUi

    //region BottomSheetUi

    @Binds
    @IntoMap
    @UiComponentKey(BottomSheetUiNormal::class, UiComponentFactory.ScreenSize.SMALL)
    abstract fun bottomSheetUiSmall(userInterface: BottomSheetUiSmall): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(BottomSheetUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun bottomSheetUiNormal(userInterface: BottomSheetUiNormal): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(BottomSheetUiNormal::class, UiComponentFactory.ScreenSize.LANDSCAPE)
    abstract fun bottomSheetUiXlarge(userInterface: BottomSheetUiLandscape): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(BottomSheetUiNormal::class, UiComponentFactory.ScreenSize.SPLIT_PORTRAIT)
    abstract fun bottomSheetUiSplitP(userInterface: BottomSheetUiSplitP): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(BottomSheetUiNormal::class, UiComponentFactory.ScreenSize.SPLIT_LANDSCAPE)
    abstract fun bottomSheetUiSplitL(userInterface: BottomSheetUiSplitL): BaseUiComponent<Context, *, *>


    //endregion BottomSheetUi

    //region dfOneButton

    @Binds
    @IntoMap
    @UiComponentKey(RvListDialogUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun dfOneButtonNormal(userInterface: RvListDialogUiNormal): BaseUiComponent<Context, *, *>

    //endregion dfOneButton

    //region getString

    @Binds
    @IntoMap
    @UiComponentKey(GetNameDialogUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun getStringUiNormal(userInterface: GetNameDialogUiNormal): BaseUiComponent<Context, *, *>

    //endregion getString

    //region BaseRvListUi

    @Binds
    @IntoMap
    @UiComponentKey(RvListUiComponent::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun baseRvListUi(userInterface: RvListUiComponent): BaseUiComponent<Context, *, *>

    //endregion BaseRvListUi

    //region PreviewerUi

    @Binds
    @IntoMap
    @UiComponentKey(PreviewerUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun previewerUiNormal(userInterface: PreviewerUiNormal): BaseUiComponent<Context, *, *>

    //endregion PreviewerUi

    //region queueItemLayout

    @Binds
    @IntoMap
    @UiComponentKey(RvQueueUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun queueItemLayoutNormal(userInterface: RvQueueUiNormal): BaseUiComponent<Context, *, *>

    //endregion queueItemLayout

    //region rvLinearFourPic

    @Binds
    @IntoMap
    @UiComponentKey(RvLinearFourPicUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun rvLinearFourPicNormal(userInterface: RvLinearFourPicUiNormal): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(RvLinearFourPicUiNormal::class, UiComponentFactory.ScreenSize.LARGE)
    abstract fun rvLinearFourPicLarge(userInterface: RvLinearFourPicUiLarge): BaseUiComponent<Context, *, *>

    //endregion rvLinearFourPic

    //region rvLinearOnePic

    @Binds
    @IntoMap
    @UiComponentKey(RvLinearOnePicUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun rvLinearOnePicNormal(userInterface: RvLinearOnePicUiNormal): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(RvLinearOnePicUiNormal::class, UiComponentFactory.ScreenSize.LARGE)
    abstract fun rvLinearOnePicLarge(userInterface: RvLinearOnePicUiLarge): BaseUiComponent<Context, *, *>

    //endregion rvLinearOnePic

    //region rvListHeader

    @Binds
    @IntoMap
    @UiComponentKey(RvListHeaderUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun rvListHeaderNormal(userInterface: RvListHeaderUiNormal): BaseUiComponent<Context, *, *>

    @Binds
    @IntoMap
    @UiComponentKey(RvListHeaderUiNormal::class, UiComponentFactory.ScreenSize.LARGE)
    abstract fun rvListHeaderLarge(userInterface: RvListHeaderUiLarge): BaseUiComponent<Context, *, *>

    //endregion rvListHeader

    //region rvGrid3Span

    @Binds
    @IntoMap
    @UiComponentKey(RvGridUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun rvGrid3Span(userInterface: RvGridUiNormal): BaseUiComponent<Context, *, *>

    //endregion rvGrid3Span

    //region AboutUiNormal

    @Binds
    @IntoMap
    @UiComponentKey(AboutUiNormal::class, UiComponentFactory.ScreenSize.NORMAL)
    abstract fun aboutUiNormal(userInterface: AboutUiNormal): BaseUiComponent<Context, *, *>

    //endregion AboutUiNormal

}

