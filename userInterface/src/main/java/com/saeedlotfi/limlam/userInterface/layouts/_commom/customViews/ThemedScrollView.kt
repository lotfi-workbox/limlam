package com.saeedlotfi.limlam.userInterface.layouts._commom.customViews

import android.content.Context
import android.widget.ScrollView
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseViewGroup

class ThemedScrollView(override var ctx: Context) : ScrollView(ctx),
    BaseViewGroup<Context, ScrollView>, ThemeManager.ThemeChangedListener {

    private var onThemeChange: ((theme: com.saeedlotfi.limlam.domain.model.ThemeDoModel) -> Unit)? = null

    var currentTheme: com.saeedlotfi.limlam.domain.model.ThemeDoModel? = null

    init {
        ThemeManager.addListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ThemeManager.addListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (ThemeManager.theme != currentTheme) {
            onThemeChanged(ThemeManager.theme)
        }
        ThemeManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ThemeManager.removeListener(this)
    }

    override fun onThemeChanged(theme: com.saeedlotfi.limlam.domain.model.ThemeDoModel) {
        currentTheme = theme
        onThemeChange?.invoke(theme)
    }

    fun setOnThemeChangeListener(onThemeChange: (theme: com.saeedlotfi.limlam.domain.model.ThemeDoModel) -> Unit) {
        this.onThemeChange = onThemeChange
    }

    class LParams(width: Int, height: Int) : LayoutParams(width, height) {
        companion object {
            const val MATCH_PARENT = -1
            const val WRAP_CONTENT = -2
        }
    }

}