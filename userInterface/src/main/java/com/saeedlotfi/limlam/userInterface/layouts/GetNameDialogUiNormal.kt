package com.saeedlotfi.limlam.userInterface.layouts

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts.GetNameDialogUiNormal.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import javax.inject.Inject

open class GetNameDialogUiNormal @Inject constructor() :
    BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var btOk: Button
    lateinit var btCancel: Button
    lateinit var tvTitle: TextView
    lateinit var etName: EditText

    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {

            cardView {
                setOnThemeChangeListener { theme ->
                    setCardBackgroundColor(Color.parseColor(theme.backgrounds))
                }
                cardElevation = dimens.elevationSize.toFloat()
                radius = dimens.cornerRadius.toFloat()

                constraintLayout {
                    id = generateViewId()

                    tvTitle = textView {
                        setOnThemeChangeListener { theme ->
                            setBackgroundColor(Color.parseColor(theme.topOfWindows))
                            setTextColor(Color.parseColor(theme.topOfWindowTexts))
                        }
                        text = ctx.getString(R.string.enter_name)
                        textSize = dimens.titleFontSize
                        gravity = Gravity.CENTER
                        setPadding(dip(10))
                    }.lParams<_, constraint_lp>(matchParent, wrapContent) {
                        topToTop = this@constraintLayout.id
                        leftToLeft = this@constraintLayout.id
                        rightToRight = this@constraintLayout.id
                    }

                    etName = editText {
                        setOnThemeChangeListener { theme ->
                            setTextColor(Color.parseColor(theme.textsAndIcons))
                            backgroundTintList =
                                ColorStateList.valueOf(Color.parseColor(theme.textsAndIcons))
                        }
                        setSelectAllOnFocus(true)
                        textSize = dimens.nameFontSize
                    }.lParams<_, constraint_lp>(matchParent, wrapContent) {
                        bottomToTop = getNextViewId()
                        leftToLeft = this@constraintLayout.id
                        rightToRight = this@constraintLayout.id
                        leftMargin = dip(10)
                        rightMargin = dip(10)
                    }

                    btOk = button {
                        id = generateViewId()
                        setOnThemeChangeListener { theme ->
                            setTextColor(Color.parseColor(theme.textsAndIcons))
                        }
                        background = Ripple.init(background)
                        text = ctx.getString(android.R.string.ok)
                        textSize = 12f
                    }.lParams<_, constraint_lp>(wrapContent, dip(50)) {
                        bottomToBottom = this@constraintLayout.id
                        bottomMargin = dip(10)
                        rightToRight = this@constraintLayout.id
                        rightMargin = dip(10)
                    }

                    btCancel = button {
                        setOnThemeChangeListener { theme ->
                            setTextColor(Color.parseColor(theme.textsAndIcons))
                        }
                        background = Ripple.init(background)
                        text = ctx.getString(android.R.string.cancel)
                        textSize = 12f
                    }.lParams<_, constraint_lp>(wrapContent, dip(50)) {
                        topToTop = btOk.id
                        bottomToBottom = btOk.id
                        rightToLeft = btOk.id
                    }

                }.lParams<_, card_lp>(matchParent, matchParent)

            }.lParams<_, frame_lp>(dimens.width, dimens.height) {
                leftMargin = dip(10)
                rightMargin = dip(10)
                gravity = Gravity.CENTER
            }

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = dip(300),
            height = dip(180),
            cornerRadius = dip(20),
            elevationSize = dip(10),
            titleFontSize = 17f,
            nameFontSize = 14f
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        val cornerRadius: Int,
        val elevationSize: Int,
        val titleFontSize: Float,
        val nameFontSize: Float,
    ) : BaseUiComponent.Dimens()

}
