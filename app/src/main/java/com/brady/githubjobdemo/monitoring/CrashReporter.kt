package com.brady.githubjobdemo.monitoring

interface CrashReporter {
    fun logMessage(message: String)
    fun logException(message: String, ex: Exception)
}
