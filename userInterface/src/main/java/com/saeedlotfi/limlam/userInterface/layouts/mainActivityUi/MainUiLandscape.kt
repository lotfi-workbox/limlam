package com.saeedlotfi.limlam.userInterface.layouts.mainActivityUi

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts.bottomSheetUi.BottomSheetUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.components.*
import javax.inject.Inject

class MainUiLandscape @Inject constructor(
    override var bottomSheetUi: UiComponentFactory<BottomSheetUiNormal>
) : MainUiNormal(bottomSheetUi) {

    lateinit var cmptFlat: FlatUiComponent

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        drawerLayout {
            dlMain = this
            setBackgroundColor(Color.TRANSPARENT)
            setScrimColor(Color.TRANSPARENT)

            drawerLayout {

                coordinatorLayout {
                    setOnThemeChangeListener { theme ->
                        setBackgroundColor(Color.parseColor(theme.windowBackground))
                    }

                    coordinatorLayout {
                        setOnThemeChangeListener { theme ->
                            setBackgroundColor(Color.parseColor(theme.windowBackground))
                        }

                        appBarLayout {
                            appBarLayout = this
                            setOnThemeChangeListener { theme ->
                                setBackgroundColor(Color.parseColor(theme.backgrounds))
                            }
                            stateListAnimator = stateListElevation(this, dimens.toolbarShadowSize)

                            collapsingToolbarLayout {
                                setOnThemeChangeListener { theme ->
                                    contentScrim = ColorDrawable(Color.parseColor(theme.backgrounds))
                                }

                                toolbar {
                                    setBackgroundColor(Color.TRANSPARENT)

                                    include<_, IconUiComponent>(dimens.menuDimens) {
                                        setImageResource(R.drawable.ic_menu)
                                        rootView.setOnClickListener {
                                            openDrawer(GravityCompat.START)
                                        }
                                    }.lParams<_, toolbar_lp>()

                                    textView {
                                        setOnThemeChangeListener { theme ->
                                            setTextColor(Color.parseColor(theme.textsAndIcons))
                                        }
                                        text = context.getString(R.string.app_name)
                                        textSize = dimens.titleSize
                                        gravity = Gravity.CENTER or Gravity.START
                                    }.lParams<_, toolbar_lp>(wrapContent, matchParent) {
                                        marginStart = dimens.menuDimens.width
                                    }

                                    cmptFlat = include<_, FlatUiComponent>(dimens.flatDimens!!)
                                        .lParams<_, toolbar_lp> {
                                            marginStart = dip(10)
                                            marginEnd = dimens.searchDimens.width
                                        }

                                    cmptSearch = include<_, SearchUiComponent>(dimens.searchDimens)
                                        .lParams<_, toolbar_lp> {
                                            gravity = Gravity.END
                                        }

                                }.lParams<_, colToolbar_lp>(
                                    displayMetrics.widthPixels, dimenAttr(R.attr.actionBarSize)
                                ) {
                                    collapseMode =
                                        CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                                }

                            }.lParams<_, appBar_lp>(matchParent, matchParent) {
                                scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                                        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                            }

                            tabLayout {
                                tabLayout = this
                                setOnThemeChangeListener { theme ->
                                    setTabTextColors(
                                        Color.parseColor(theme.textHint),
                                        Color.parseColor(theme.textsAndIcons)
                                    )
                                    tabIconTint =
                                        ColorStateList.valueOf(Color.parseColor(theme.textsAndIcons))
                                    tabRippleColor =
                                        ColorStateList.valueOf(Color.parseColor(theme.rippleColor))
                                    setSelectedTabIndicatorColor(Color.parseColor(theme.textsAndIcons))
                                }
                                setBackgroundColor(Color.TRANSPARENT)
                                tabMode = TabLayout.MODE_SCROLLABLE
                            }.lParams<_, appBar_lp> {
                                scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                            }

                        }.lParams<_, coordinator_lp>(displayMetrics.widthPixels)

                        viewPager {
                            viewPager = this
                            id = View.generateViewId()
                            setOnThemeChangeListener { theme ->
                                setBackgroundColor(
                                    Color.parseColor(
                                        theme.windowBackground
                                    )
                                )
                            }
                            offscreenPageLimit = 7
                        }.lParams<_, coordinator_lp>(displayMetrics.widthPixels, matchParent) {
                            behavior = AppBarLayout.ScrollingViewBehavior()
                        }

                    }.lParams<_, coordinator_lp>(matchParent, matchParent)

                    bottomSheet = include(R.layout.bottom_sheet) {

                        cmptBottomSheet = include(bottomSheetUi){
                            cmptFlat = this@MainUiLandscape.cmptFlat
                        }.lParams<_, frame_lp>()

                    }

                }.lParams<_, drawer_lp>(matchParent, matchParent)

                cmptNavigation = include<_, NavigationUiComponent>(dimens.navigationDimens)
                    .lParams<_, drawer_lp> {
                        gravity = GravityCompat.START
                    }

            }

            cmptThemeChanger = include<_, ThemeChangerUiComponent>(dimens.themeChangerDimens)
                .lParams<_, drawer_lp>()

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            titleSize = 30f,
            toolbarShadowSize = dip(20),
            searchDimens = SearchUiComponent.Dimens(
                width = dip(50),
                height = matchParent
            ),
            flatDimens = FlatUiComponent.Dimens(
                width = matchParent,
                height = dip(50),
                strokeSize = dip(1),
                cornerRadius = dip(10),
                thumbnailDimens = ThumbnailUiComponent.Dimens(
                    width = dip(44),
                    height = dip(44)
                ),
                flatTitleDimens = TitleUiComponent.Dimens(
                    width = matchParent,
                    height = dip(45)
                ),
                flatControllerDimens = ControllerUiComponent.Dimens(
                    width = dip(90),
                    height = dip(30)
                )
            ),
            menuDimens = IconUiComponent.Dimens(
                width = dip(35),
                height = dip(35)
            ),
            themeChangerDimens = ThemeChangerUiComponent.Dimens(
                width = matchParent,
                height = matchParent
            ),
            navigationDimens = NavigationUiComponent.Dimens(
                width = dip(220),
                height = matchParent,
                nightDimens = IconUiComponent.Dimens(
                    width = dip(25),
                    height = dip(25)
                )
            )
        )
    }

}

