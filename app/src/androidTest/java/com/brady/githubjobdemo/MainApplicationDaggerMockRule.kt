package com.brady.githubjobdemo

import com.brady.githubjobdemo.TestUtils.getAppUnderTest
import com.brady.githubjobdemo.app.AndroidModule
import com.brady.githubjobdemo.app.AppModule
import com.brady.githubjobdemo.app.ApplicationComponent
import com.brady.githubjobdemo.app.VariantModule
import com.brady.githubjobdemo.data.DataModule
import com.brady.githubjobdemo.modules.CrashReporterModule
import com.brady.githubjobdemo.monitoring.LoggerModule
import it.cosenonjaviste.daggermock.DaggerMockRule

class MainApplicationDaggerMockRule : DaggerMockRule<ApplicationComponent>(
        ApplicationComponent::class.java,
        VariantModule(),
        AndroidModule(getAppUnderTest()),
        AppModule(0),
        LoggerModule(),
        CrashReporterModule(),
        DataModule()) {
    init {
        set { component -> getAppUnderTest().component = component }
    }
}
