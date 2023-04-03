package com.saeedlotfi.limlam.domain.model

data class ThemeDoModel(

    var name : String = "Default",

    var backgrounds : String = "#F8F8F8",

    var selectionBackgrounds : String = "#FFA0D5FF",

    var paleBackgrounds : String = "#EFEFEF",

    var windowBackground : String = "#FDFDFD",

    var textsAndIcons : String = "#1C1C1C",

    var textHint : String = "#74000000",

    var strokeColor : String = "#FFFFFF",

    var topOfWindows : String = "#424A60",

    var topOfWindowTexts : String = "#FFFFFF",

    var undoBar : String = "#222222",

    var startShadow : String = "#00000000",

    var endShadow : String = "#2C000000",

    var rippleColor : String = "#BE808080",

    var id : Long = System.currentTimeMillis()

)