package com.saeedlotfi.limlam.userInterface.layouts._commom.customViews

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseView

class ThemedRecyclerView(override var ctx: Context) : RecyclerView(ctx),
    BaseView<Context, RecyclerView>, ThemeManager.ThemeChangedListener {

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

}