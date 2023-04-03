package com.saeedlotfi.limlam.dependencyInjection

import android.app.Application
import com.saeedlotfi.limlam.data.dependencyInjection.DataModule
import com.saeedlotfi.limlam.userInterface.dependencyInjection.UserInterfaceComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class, DataModule::class]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent

        @BindsInstance
        fun application(application: Application): Builder

    }

    fun userInterfaceComponent(): UserInterfaceComponent.Factory

}