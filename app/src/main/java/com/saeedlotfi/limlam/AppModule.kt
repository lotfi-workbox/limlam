package com.saeedlotfi.limlam

import android.app.Application
import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.dependencyInjection.ApplicationComponent
import com.saeedlotfi.limlam.dependencyInjection.DaggerApplicationComponent
import com.saeedlotfi.limlam.userInterface.dependencyInjection.UserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.UserInterfaceComponentProvider

class LimLam : Application(),
    UserInterfaceComponentProvider {

    private var applicationComponent: ApplicationComponent =
        DaggerApplicationComponent.builder().application(this).build()

    override fun onCreate() {
        RealmManager.initAndConfig(this)
        super.onCreate()
    }

    override fun getUserInterfaceComponent(): UserInterfaceComponent {
        return applicationComponent.userInterfaceComponent().create()
    }

}

