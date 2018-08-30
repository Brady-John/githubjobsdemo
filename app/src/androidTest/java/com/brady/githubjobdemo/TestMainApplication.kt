package com.brady.githubjobdemo

import com.brady.githubjobdemo.app.MainApplication

class TestMainApplication : MainApplication() {

    override fun initializeApplication() {
        // Don't initialize the application
    }

    override fun isTesting() = true
}
