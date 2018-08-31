package com.brady.githubjobdemo.data.api.github

import com.brady.githubjobdemo.data.api.github.model.Job

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("positions.json?")
    fun listJobs() : Single<Response<List<Job>>>
}
