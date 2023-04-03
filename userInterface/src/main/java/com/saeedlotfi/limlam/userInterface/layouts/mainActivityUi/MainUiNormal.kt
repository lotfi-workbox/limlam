@file:Suppress("MemberVisibilityCanBePrivate")

package com.saeedlotfi.limlam.userInterface.layouts.mainActivityUi

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface._common.*
import com.saeedlotfi.limlam.userInterface.activities.main.MainTabAdapter
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.*
import com.saeedlotfi.limlam.userInterface.layouts.bottomSheetUi.BottomSheetUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.components.*
import com.saeedlotfi.limlam.userInterface.layouts.mainActivityUi.MainUiNormal.*
import com.saeedlotfi.limlam.userInterface.viewModels.MainViewModel
import javax.inject.Inject

open class MainUiNormal @Inject constructor(
    protected open var bottomSheetUi: UiComponentFactory<BottomSheetUiNormal>
) : BaseUiComponent<Context, ThemedDrawerLayout, Dimens>() {

    override lateinit var rootView: ThemedDrawerLayout
    lateinit var viewModel: MainViewModel

    lateinit var dlMain: ThemedDrawerLayout
    lateinit var bottomSheet: ThemedFrameLayout
    lateinit var appBarLayout: ThemedAppBarLayout
    lateinit var tabLayout: ThemedTabLayout
    lateinit var viewPager: ThemedViewPager
    lateinit var cmptSearch: SearchUiComponent
    lateinit var cmptThemeChanger: ThemeChangerUiComponent
    lateinit var cmptBottomSheet: BottomSheetUiNormal
    lateinit var cmptNavigation: NavigationUiComponent

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
                                    contentScrim =
                                        ColorDrawable(Color.parseColor(theme.backgrounds))
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
                                scrollFlags =
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
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
                            bottomMargin = dip(70)
                            behavior = AppBarLayout.ScrollingViewBehavior()
                        }

                    }.lParams<_, coordinator_lp>()

                    bottomSheet = include(R.layout.bottom_sheet) {

                        cmptBottomSheet = include(bottomSheetUi)
                            .lParams<_, frame_lp>()

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

    fun configViews(fragmentManager: FragmentManager) {
        bottomSheetUi.view.viewModel = viewModel
        bottomSheetUi.view.configBottomSheetBehavior(bottomSheet)
        bottomSheetUi.view.configBottomSheet(fragmentManager)
        MainTabAdapter.configWithTabLayout(viewPager, tabLayout, fragmentManager)
        cmptNavigation.configNavigationView(fragmentManager)
        cmptThemeChanger.circularAnimation(rootView, cmptNavigation.btNight) {
            viewModel.changeNightMode()
        }
        cmptSearch.configSearchView {
            (viewPager.adapter!!.instantiateItem(viewPager, viewPager.currentItem) as FilterAdapter)
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
            flatDimens = null,
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
                    width = dip(30),
                    height = dip(30)
                )
            )
        )
    }

    class Dimens(
        var titleSize: Float,
        var toolbarShadowSize: Int,
        var searchDimens: SearchUiComponent.Dimens,
        var flatDimens: FlatUiComponent.Dimens?,
        val menuDimens: IconUiComponent.Dimens,
        var themeChangerDimens: ThemeChangerUiComponent.Dimens,
        var navigationDimens: NavigationUiComponent.Dimens,
    ) : BaseUiComponent.Dimens()

}