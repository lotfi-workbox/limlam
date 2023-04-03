package com.saeedlotfi.limlam.userInterface.layouts.bottomSheetUi

import android.content.Context
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface._common.MusicMetadataExtractor
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.dip
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.displayMetrics
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.matchParent
import com.saeedlotfi.limlam.userInterface.layouts.components.*
import javax.inject.Inject

class BottomSheetUiSmall @Inject constructor(
    override var mediaPlayerCore: MediaPlayerCore,
    override var metadataExtractor: MusicMetadataExtractor
) : BottomSheetUiNormal(mediaPlayerCore, metadataExtractor) {

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = matchParent,
            height = displayMetrics.heightPixels,
            shadowSize = dip(15),
            flatDimens = FlatUiComponent.Dimens(
                width = matchParent,
                height = dip(70),
                strokeSize = 0,
                cornerRadius = 0,
                thumbnailDimens = ThumbnailUiComponent.Dimens(
                    width = dip(65),
                    height = dip(65)
                ),
                flatTitleDimens = TitleUiComponent.Dimens(
                    width = matchParent,
                    height = dip(65)
                ),
                flatControllerDimens = ControllerUiComponent.Dimens(
                    width = dip(120),
                    height = dip(40)
                )
            ),
            imageSize = displayMetrics.widthPixels / 2,
            titleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(70)
            ),
            seekbarDimens = SeekbarUiComponent.Dimens(
                width = matchParent,
                height = dip(60)
            ),
            dashboardDimens = DashboardUiComponent.Dimens(
                width = matchParent,
                height = dip(50)
            ),
            controllerDimens = ControllerUiComponent.Dimens(
                width = dip(200),
                height = dip(60)
            ),
        )
    }

}