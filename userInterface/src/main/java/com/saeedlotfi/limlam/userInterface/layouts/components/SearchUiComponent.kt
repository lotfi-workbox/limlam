package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.children
import com.saeedlotfi.limlam.userInterface._common.FilterAdapter
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedSearchView
import com.saeedlotfi.limlam.userInterface.layouts.components.SearchUiComponent.*
import javax.inject.Inject

open class SearchUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedSearchView, Dimens>() {

    override lateinit var rootView: ThemedSearchView

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        searchView {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            setOnThemeChangeListener { theme ->
                (findViewById<View>(com.google.android.material.R.id.search_button) as ImageView).also { icon ->
                    val whiteIcon: Drawable = icon.drawable
                    whiteIcon.setTint(Color.parseColor(theme.textsAndIcons))
                    icon.setImageDrawable(whiteIcon)
                }
                (findViewById<View>(com.google.android.material.R.id.search_src_text) as TextView).also { text ->
                    text.setTextColor(Color.parseColor(theme.textsAndIcons))
                }
                (findViewById<View>(com.google.android.material.R.id.search_close_btn) as ImageView).also { icon ->
                    val whiteIcon: Drawable = icon.drawable
                    whiteIcon.setTint(Color.parseColor(theme.textHint))
                    icon.setImageDrawable(whiteIcon)
                }
                (findViewById<View>(com.google.android.material.R.id.search_plate) as LinearLayout).also { frame ->
                    frame.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(theme.textHint))
                }
                SearchView::class.java.getDeclaredField("mSearchHintIcon").also { hintIcon ->
                    hintIcon.isAccessible = true
                    (hintIcon.get(this) as Drawable).colorFilter =
                        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                            Color.parseColor(theme.textHint),
                            BlendModeCompat.SRC_ATOP
                        )
                }
            }
        }
    }

    fun configSearchView(getFilterAdapter: () -> FilterAdapter) = with(rootView) {
        var filterAdapter: FilterAdapter? = null
        var width = wrapContent

        fun filter(text: String) {
            getFilterAdapter().also {
                if (filterAdapter !== it) {
                    filterAdapter?.filter("")
                    filterAdapter = it
                }
                filterAdapter?.filter(text)
            }
        }

        setOnQueryTextFocusChangeListener { _, hasFocus ->
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!isIconified && hasFocus) {
                            filter(newText ?: "")
                        }
                        return false
                    }
                })
        }

        setOnCloseListener {
            layoutParams.width = width
            (parent as ViewGroup).children.forEach {
                it.visibility = View.VISIBLE
            }
            filter("")
            false
        }

        setOnSearchClickListener { v ->
            width = layoutParams.width
            (parent as ViewGroup).children.forEach {
                if (v !== it) it.visibility = View.GONE
            }
            layoutParams.width = matchParent
        }

    }

    override fun getDimens(uiContext: UiContext<Context>): Dimens = Dimens(
        width = wrapContent,
        height = wrapContent
    )

    class Dimens(override var width: Int, override var height: Int) : BaseUiComponent.Dimens()

}