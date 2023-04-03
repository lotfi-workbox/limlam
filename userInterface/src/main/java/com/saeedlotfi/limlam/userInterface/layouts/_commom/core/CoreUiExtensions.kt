@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.saeedlotfi.limlam.userInterface.layouts._commom.core

import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.Dimension
import androidx.cardview.widget.CardView
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseUiComponent.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.*

typealias vGroup_lp = BaseViewGroup.LParams
typealias appBar_lp = ThemedAppBarLayout.LParams
typealias card_lp = FrameLayout.LayoutParams
typealias colToolbar_lp = ThemedCollapsingToolbarLayout.LParams
typealias constraint_lp = ThemedConstraintLayout.LParams
typealias coordinator_lp = ThemedCoordinatorLayout.LParams
typealias drawer_lp = ThemedDrawerLayout.LParams
typealias frame_lp = ThemedFrameLayout.LParams
typealias linear_lp = ThemedLinearLayout.LParams
typealias scroll_lp = ThemedScrollView.LParams
typealias toolbar_lp = ThemedToolbar.LParams

inline val matchParent: Int get() = LayoutParams.MATCH_PARENT
inline val wrapContent: Int get() = LayoutParams.WRAP_CONTENT

inline fun <reified T : UiContext<*>> T.appBarLayout(
    scope: ThemedAppBarLayout.() -> Unit
): ThemedAppBarLayout {
    return ThemedAppBarLayout(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.cardView(
    scope: ThemedCardView.() -> Unit
): ThemedCardView {
    return ThemedCardView(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.collapsingToolbarLayout(
    scope: ThemedCollapsingToolbarLayout.() -> Unit
): ThemedCollapsingToolbarLayout {
    return ThemedCollapsingToolbarLayout(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.constraintLayout(
    scope: ThemedConstraintLayout.() -> Unit
): ThemedConstraintLayout {
    return ThemedConstraintLayout(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.coordinatorLayout(
    scope: ThemedCoordinatorLayout.() -> Unit
): ThemedCoordinatorLayout {
    return ThemedCoordinatorLayout(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.drawerLayout(
    scope: ThemedDrawerLayout.() -> Unit
): ThemedDrawerLayout {
    return ThemedDrawerLayout(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.frameLayout(
    scope: ThemedFrameLayout.() -> Unit
): ThemedFrameLayout {
    return ThemedFrameLayout(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.linearLayout(
    scope: ThemedLinearLayout.() -> Unit
): ThemedLinearLayout {
    return ThemedLinearLayout(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.scrollView(
    scope: ThemedScrollView.() -> Unit
): ThemedScrollView {
    return ThemedScrollView(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.toolbar(
    scope: ThemedToolbar.() -> Unit
): ThemedToolbar {
    return ThemedToolbar(this.ctx).also {
        scope(it)
        if (this is ViewGroup) this.addView(it)
    }
}

inline fun <reified T : UiContext<*>> T.button(
    scope: ThemedButton.() -> Unit
): ThemedButton {
    return ThemedButton(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.checkBox(
    scope: ThemedCheckBox.() -> Unit
): ThemedCheckBox {
    return ThemedCheckBox(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.editText(
    scope: ThemedEditText.() -> Unit
): ThemedEditText {
    return ThemedEditText(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.imageButton(
    scope: ThemedImageButton.() -> Unit
): ThemedImageButton {
    return ThemedImageButton(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.imageView(
    scope: ThemedImageView.() -> Unit
): ThemedImageView {
    return ThemedImageView(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.navigationView(
    scope: ThemedNavigationView.() -> Unit
): ThemedNavigationView {
    return ThemedNavigationView(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.progressBar(
    scope: ThemedProgressBar.() -> Unit
): ThemedProgressBar {
    return ThemedProgressBar(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.recyclerView(
    scope: ThemedRecyclerView.() -> Unit
): ThemedRecyclerView {
    return ThemedRecyclerView(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.searchView(
    scope: ThemedSearchView.() -> Unit
): ThemedSearchView {
    return ThemedSearchView(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.seekBar(
    scope: ThemedSeekBar.() -> Unit
): ThemedSeekBar {
    return ThemedSeekBar(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.tabLayout(
    scope: ThemedTabLayout.() -> Unit
): ThemedTabLayout {
    return ThemedTabLayout(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.textView(
    scope: ThemedTextView.() -> Unit
): ThemedTextView {
    return ThemedTextView(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.view(
    scope: ThemedView.() -> Unit
): ThemedView {
    return ThemedView(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.viewPager(
    scope: ThemedViewPager.() -> Unit
): ThemedViewPager {
    return ThemedViewPager(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

inline fun <reified T : UiContext<*>> T.roundImageView(
    scope: RoundImageView.() -> Unit
): RoundImageView {
    return RoundImageView(this.ctx).also {
        if (this is ViewGroup) this.addView(it)
        scope(it)
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <T : BaseView<*, *>, reified LP : LayoutParams> T.lParams(
    width: Int? = null,
    height: Int? = null,
    noinline init: (LP.() -> Unit)? = null
): T {
    val instance = LP::class.java
        .getDeclaredConstructor(Int::class.java, Int::class.java)
        .newInstance(width ?: wrapContent, height ?: wrapContent)
    init?.invoke(instance as LP)
    (this as BaseView<Context, View>).setLParams(this as View, instance as LP)
    return this
}

@Suppress("UNCHECKED_CAST")
inline fun <T : BaseUiComponent<*, *, *>, reified LP : LayoutParams> T.lParams(
    noinline init: (LP.() -> Unit)? = null
): T {
    var lParams = (rootView as BaseView<Context, View>).getLParams(rootView)
    if (lParams !is LP) lParams = LP::class.java
        .getDeclaredConstructor(Int::class.java, Int::class.java)
        .newInstance(lParams.width, lParams.height)
    init?.invoke(lParams as LP)
    (rootView as BaseView<Context, View>).setLParams(rootView, lParams)
    return this
}

inline fun <T : View> UiContext<*>.include(layoutId: Int, init: T.() -> Unit): T {
    @Suppress("UNCHECKED_CAST")
    return layoutInflater.inflate(
        layoutId,
        this as ViewGroup,
        false
    ).also {
        addView(it)
        init(it as T)
    } as T
}

inline fun <D : Dimens, reified T : BaseUiComponent<Context, *, D>> UiContext<*>.include(
    dimens: D, noinline init: (T.() -> Unit)? = null
): T {
    return T::class.java.newInstance().also { component ->
        @Suppress("UNCHECKED_CAST")
        component.initRootView(this as UiContext<Context>, dimens)
        init?.invoke(component)
    }
}

inline fun <D : Dimens, reified T : BaseUiComponent<Context, *, D>,
        CF : UiComponentFactory<T>> UiContext<*>.include(
    component: CF, noinline init: (T.() -> Unit)? = null
): T {
    @Suppress("UNCHECKED_CAST")
    component.createComponent(this as UiContext<Context>, T::class)
    init?.invoke(component.view)
    return component.view
}

inline fun <reified R : LayoutParams> generateLayoutParams(): R {
    return R::class.java.getDeclaredConstructor(Int::class.java, Int::class.java)
        .newInstance(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) as R
}

@Dimension(unit = Dimension.PX)
inline fun UiContext<*>.dimenAttr(@AttrRes attribute: Int): Int {
    val typedValue = TypedValue()
    if (!ctx.theme.resolveAttribute(attribute, typedValue, true)) {
        throw IllegalArgumentException("Failed to resolve attribute: $attribute")
    }
    return TypedValue.complexToDimensionPixelSize(typedValue.data, ctx.resources.displayMetrics)
}

inline val UiContext<*>.layoutInflater: android.view.LayoutInflater
    get() = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater

inline val UiContext<*>.displayMetrics: DisplayMetrics
    get() = ctx.resources.displayMetrics

inline fun UiContext<*>.dip(value: Int): Int =
    (value * ctx.resources.displayMetrics.density).toInt()

inline fun UiContext<*>.toast(text: CharSequence) =
    Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show()

inline fun <T> T.setPadding(value: Int) where T : BaseView<*, *>, T : View =
    setPadding(value, value, value, value)

inline fun <T> T.setContentPadding(value: Int) where T : BaseView<*, *>, T : CardView =
    setContentPadding(value, value, value, value)

inline fun topShadow(theme: com.saeedlotfi.limlam.domain.model.ThemeDoModel) = GradientDrawable().apply {
    shape = GradientDrawable.RECTANGLE
    orientation = GradientDrawable.Orientation.TOP_BOTTOM
    colors = intArrayOf(
        Color.parseColor(theme.startShadow),
        Color.parseColor(theme.endShadow)
    )
}

inline fun stateListElevation(target: View, size: Int) = StateListAnimator().also {
    it.addState(intArrayOf(0), ObjectAnimator.ofFloat(target, "elevation", size.toFloat()))
}

inline fun UiContext<*>.getStatusBarHeight(): Int {
    val frameInfo = Rect()
    (ctx as Activity).window.decorView.getWindowVisibleDisplayFrame(frameInfo)
    return frameInfo.top
}

private var generatedViewIds = IntArray(2) { View.generateViewId() }

fun ThemedConstraintLayout.generateViewId(): Int {
    val id = generatedViewIds[0]
    generatedViewIds[0] = generatedViewIds[1]
    generatedViewIds[1] = View.generateViewId()
    return id
}

fun ThemedConstraintLayout.getNextViewId() = generatedViewIds[0]


