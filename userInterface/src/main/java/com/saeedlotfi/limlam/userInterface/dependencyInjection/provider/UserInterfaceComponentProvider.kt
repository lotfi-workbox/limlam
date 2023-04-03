package com.saeedlotfi.limlam.userInterface.dependencyInjection.provider

import android.app.Activity
import android.app.Service
import android.content.Context
import androidx.fragment.app.Fragment
import com.saeedlotfi.limlam.userInterface.dependencyInjection.UserInterfaceComponent

interface UserInterfaceComponentProvider {
    fun getUserInterfaceComponent(): UserInterfaceComponent
}

fun <T> T.getUserInterfaceComponent(context: Context? = null): UserInterfaceComponent {
    return when (this) {
        is Fragment -> {
            (this.requireContext().applicationContext as UserInterfaceComponentProvider).getUserInterfaceComponent()
        }
        is Activity -> {
            (this.applicationContext as UserInterfaceComponentProvider).getUserInterfaceComponent()
        }
        is Service -> {
            (this.applicationContext as UserInterfaceComponentProvider).getUserInterfaceComponent()
        }
        else -> {
            if (context != null) (context.applicationContext as UserInterfaceComponentProvider).getUserInterfaceComponent()
            else throw Exception("there is no information about context")
        }
    }
}
