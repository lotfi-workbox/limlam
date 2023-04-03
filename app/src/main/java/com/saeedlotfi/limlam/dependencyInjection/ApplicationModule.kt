package com.saeedlotfi.limlam.dependencyInjection


import com.saeedlotfi.limlam.userInterface.dependencyInjection.UserInterfaceComponent
import dagger.Module

@Module(subcomponents = [UserInterfaceComponent::class])
class ApplicationModule