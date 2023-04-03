@file:Suppress("MemberVisibilityCanBePrivate")

package com.saeedlotfi.limlam.userInterface.layouts

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.widget.LinearLayout
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts.AboutUiNormal.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedImageView
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedTextView
import javax.inject.Inject

open class AboutUiNormal @Inject constructor() :
    BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var ivIcon: ThemedImageView
    lateinit var tvAppName: ThemedTextView
    lateinit var tvAppVersion: ThemedTextView

    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, frame_lp>(dimens.width, dimens.height)

            cardView {
                setOnThemeChangeListener { theme ->
                    setCardBackgroundColor(Color.parseColor(theme.paleBackgrounds))
                }
                useCompatPadding = true
                setPadding(dip(12))
                setContentPadding(dip(12))
                radius = 20f
                cardElevation = dip(15).toFloat()

                scrollView {

                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        setHorizontalGravity(Gravity.CENTER)

                        ivIcon = imageView {
                            background = Ripple.init()
                            isClickable = true
                            setImageResource(R.drawable.ic_icon)
                        }.lParams<_, linear_lp>(dimens.iconSize, dimens.iconSize) {
                            topMargin = dip(30)
                        }

                        tvAppName = textView {
                            setOnThemeChangeListener { theme ->
                                setTextColor(Color.parseColor(theme.textsAndIcons))
                            }
                            gravity = Gravity.CENTER
                            textSize = 35f
                            text = ctx.getString(R.string.app_name)
                        }.lParams<_, linear_lp>(matchParent, wrapContent)

                        tvAppVersion = textView {
                            setOnThemeChangeListener { theme ->
                                setTextColor(Color.parseColor(theme.textsAndIcons))
                            }
                            gravity = Gravity.CENTER
                            textSize = 12f
                            text = ctx.getString(R.string.appVersion)
                        }.lParams<_, linear_lp>(matchParent, wrapContent)

                        textView {
                            setOnThemeChangeListener { theme ->
                                setTextColor(Color.parseColor(theme.textsAndIcons))
                                setLinkTextColor(Color.parseColor(theme.textHint))
                            }
                            textSize = 12f
                            movementMethod = LinkMovementMethod.getInstance()
                            @Suppress("DEPRECATION")
                            text = Html.fromHtml(ctx.getString(R.string.info))
                            gravity = Gravity.CENTER
                        }.lParams<_, linear_lp>(matchParent, wrapContent)

                    }.lParams<_, scroll_lp>(matchParent, matchParent)

                }.lParams<_, card_lp>(matchParent, matchParent) {
                    gravity = Gravity.CENTER or Gravity.BOTTOM
                }

            }.lParams<_, frame_lp>(dimens.width, dimens.height)
        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = dip(300),
            height = dip(400),
            iconSize = dip(140)
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        var iconSize: Int,
    ) : BaseUiComponent.Dimens()

}