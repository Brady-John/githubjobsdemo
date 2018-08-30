package com.brady.githubjobdemo

import android.support.test.InstrumentationRegistry
import com.brady.githubjobdemo.app.MainApplication

object TestUtils {
    fun getAppUnderTest(): MainApplication {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        return targetContext.applicationContext as MainApplication
    }
}
