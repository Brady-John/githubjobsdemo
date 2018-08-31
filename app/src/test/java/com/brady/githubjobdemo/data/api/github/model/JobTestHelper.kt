package com.brady.githubjobdemo.data.api.github.model

object JobTestHelper {
    fun stubJob(authorName: String, message: String): Job {
        val author = Author(authorName)
        val JobDetails = JobDetails(message, author)
        return Job(JobDetails)
    }
}
