package com.saeedlotfi.limlam.userInterface._common

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class BroadcastCenter {

    companion object {

        fun updateWidget(context: Context, wdgClass: Class<*>) {
            Intent(context, wdgClass).also { intent ->
                val awm = AppWidgetManager.getInstance(context)
                val ids = awm.getAppWidgetIds(ComponentName(context, wdgClass))
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                context.sendBroadcast(intent)
            }
        }

    }

}