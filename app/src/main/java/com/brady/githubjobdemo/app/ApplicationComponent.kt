package com.brady.githubjobdemo.app

import com.brady.githubjobdemo.data.DataModule
import com.brady.githubjobdemo.modules.CrashReporterModule
import com.brady.githubjobdemo.monitoring.LoggerModule
import com.brady.githubjobdemo.ui.ViewModelFactoryModule
import com.brady.githubjobdemo.ui.main.MainActivity
// GENERATOR - MORE IMPORTS //
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
        VariantModule::class,
        AndroidModule::class,
        AppModule::class,
        LoggerModule::class,
        CrashReporterModule::class,
        DataModule::class,
        ViewModelFactoryModule::class])
interface ApplicationComponent : VariantApplicationComponent {
    fun inject(application: MainApplication)

    fun inject(activity: MainActivity)
    // GENERATOR - MORE ACTIVITIES //
}