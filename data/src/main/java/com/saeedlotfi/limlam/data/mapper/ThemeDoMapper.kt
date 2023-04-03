package com.saeedlotfi.limlam.data.mapper

import com.saeedlotfi.limlam.data.model.ThemeDbModel
import com.saeedlotfi.limlam.domain.model.ThemeDoModel
import io.realm.RealmList

fun ThemeDoModel.asDatabaseModel(): ThemeDbModel {
    return ThemeDbModel(
        name = this.name,
        backgrounds = this.backgrounds,
        selectionBackgrounds = this.selectionBackgrounds,
        paleBackgrounds = this.paleBackgrounds,
        windowBackground = this.windowBackground,
        textsAndIcons = this.textsAndIcons,
        textHint = this.textHint,
        strokeColor = this.strokeColor,
        topOfWindows = this.topOfWindows,
        topOfWindowTexts = this.topOfWindowTexts,
        undoBar = this.undoBar,
        startShadow = this.startShadow,
        endShadow = this.endShadow,
        rippleColor = this.rippleColor,
        id = this.id,
    )
}

fun ThemeDbModel.asDomainModel(): ThemeDoModel {
    return ThemeDoModel(
        name = this.name,
        backgrounds = this.backgrounds,
        selectionBackgrounds = this.selectionBackgrounds,
        paleBackgrounds = this.paleBackgrounds,
        windowBackground = this.windowBackground,
        textsAndIcons = this.textsAndIcons,
        textHint = this.textHint,
        strokeColor = this.strokeColor,
        topOfWindows = this.topOfWindows,
        topOfWindowTexts = this.topOfWindowTexts,
        undoBar = this.undoBar,
        startShadow = this.startShadow,
        endShadow = this.endShadow,
        rippleColor = this.rippleColor,
        id = this.id,
    )
}

fun List<ThemeDoModel>.asDatabaseModel(): RealmList<ThemeDbModel> {
    return RealmList<ThemeDbModel>().also { list ->
        list.addAll(this.map { it.asDatabaseModel() })
    }
}

fun List<ThemeDbModel>.asDomainModel(): List<ThemeDoModel> = this.map { it.asDomainModel() }



