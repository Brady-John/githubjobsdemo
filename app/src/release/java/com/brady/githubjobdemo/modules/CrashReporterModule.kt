package com.brady.githubjobdemo.modules

import com.brady.githubjobdemo.monitoring.CrashlyticsCrashReporter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CrashReporterModule {
    @Singleton
    @Provides
    fun provideCrashReporter() = CrashlyticsCrashReporter()
}
