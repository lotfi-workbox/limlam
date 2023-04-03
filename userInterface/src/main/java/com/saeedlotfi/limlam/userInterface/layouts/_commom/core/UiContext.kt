package com.saeedlotfi.limlam.userInterface.layouts._commom.core

import android.content.Context

interface UiContext<T : Context> {

    var ctx: T

    companion object {
        fun <T : Context> create(context: T): UiContext<T> {
            return object : UiContext<T> {
                override var ctx: T = context
            }
        }
    }

}