package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface._common.DialogsManager
import com.saeedlotfi.limlam.userInterface.fragments.AboutDialog
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedImageButton
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedNavigationView
import com.saeedlotfi.limlam.userInterface.layouts.components.NavigationUiComponent.*
import javax.inject.Inject

open class NavigationUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedNavigationView, Dimens>() {

    override lateinit var rootView: ThemedNavigationView

    lateinit var btNight: ThemedImageButton

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        navigationView {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            itemIconTintList = null
            setOnThemeChangeListener { theme ->
                itemTextColor = ColorStateList.valueOf(Color.parseColor(theme.textsAndIcons))
                itemBackground = itemBackground?.also {
                    it.setTint(Color.parseColor(theme.textsAndIcons))
                }
                setBackgroundColor(Color.parseColor(theme.backgrounds))
                for (i in 0 until menu.size()) {
                    val menuItem: MenuItem = menu.getItem(i)
                    val spanString = SpannableString(menuItem.title.toString())
                    spanString.setSpan(
                        ForegroundColorSpan(
                            Color.parseColor(theme.textHint)
                        ), 0, spanString.length, 0
                    )
                    menuItem.title = spanString
                    for (j in 0 until menuItem.subMenu!!.size()) {
                        menuItem.subMenu!![j].icon?.setTint(Color.parseColor(theme.textsAndIcons))
                    }
                }
            }

            btNight = imageButton {
                setOnThemeChangeListener { theme ->
                    setColorFilter(Color.parseColor(theme.textsAndIcons), PorterDuff.Mode.SRC_IN)
                    background = LayerDrawable(
                        arrayOf(
                            GradientDrawable().also { gd ->
                                gd.shape = GradientDrawable.OVAL
                                gd.color = ColorStateList.valueOf(Color.TRANSPARENT)
                                gd.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                                gd.setStroke(dip(1), Color.parseColor(theme.textsAndIcons))
                            },
                            Ripple.init()
                        )
                    )
                    setImageBitmap(
                        ContextCompat.getDrawable(context, R.drawable.ic_night_mode)?.mutate()
                            ?.toBitmap(
                                dimens.nightDimens.width - dip(10),
                                dimens.nightDimens.height - dip(10)
                            )
                    )
                }
            }.lParams<_, frame_lp>(dimens.nightDimens.width, dimens.nightDimens.height) {
                gravity = Gravity.END
                topMargin = dip(20)
                marginEnd = dip(20)
            }

        }
    }

    fun configNavigationView(fragmentManager: FragmentManager) = with(rootView) {
        val drawer = parent as DrawerLayout

        fun addSubMenu(title: String) = menu.addSubMenu(
            Menu.NONE, View.generateViewId(), Menu.NONE,
            SpannableString(title).also { ss ->
                ss.setSpan(
                    ForegroundColorSpan(
                        Color.parseColor(ThemeManager.theme.textHint)
                    ), 0, ss.length, 0
                )
            }
        )

        fun SubMenu.addMenu(title: String, @DrawableRes icon: Int) =
            add(Menu.NONE, View.generateViewId(), Menu.NONE, title)
                .also { setting ->
                    setting.icon = ContextCompat.getDrawable(context, icon)
                        ?.also { drawable ->
                            DrawableCompat.setTint(
                                drawable,
                                Color.parseColor(ThemeManager.theme.textsAndIcons)
                            )
                        }
                }


        val other = addSubMenu(ctx.getString(R.string.other))

        val settings = other.addMenu(ctx.getString(R.string.settings), R.drawable.ic_settings)

        val about = other.addMenu(ctx.getString(R.string.about), R.drawable.ic_about)

        setNavigationItemSelectedListener {
            when (it.itemId) {
                settings.itemId -> {

                }
                about.itemId -> {
                    DialogsManager.showDialogWithTag(
                        fragmentManager,
                        AboutDialog(),
                        AboutDialog::class.simpleName!!
                    )
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = wrapContent,
            height = wrapContent,
            nightDimens = IconUiComponent.Dimens(
                width = dip(25),
                height = dip(25)
            )
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        val nightDimens: IconUiComponent.Dimens,
    ) : BaseUiComponent.Dimens()

}