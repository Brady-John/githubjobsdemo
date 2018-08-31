package com.brady.githubjobdemo.data.api.github

import android.content.Context
import com.brady.githubjobdemo.Mockable
import com.brady.githubjobdemo.R
import com.brady.githubjobdemo.data.api.github.model.Job

import io.reactivex.Observable
import retrofit2.Response
import timber.log.Timber

@Mockable
class GitHubInteractor(
        private val context: Context,
        private val api: GitHubApiService) {

    class LoadJobsResponse( val jobs: List<Job>)

    fun loadJobs(): Observable<LoadJobsResponse> {
        return api.listJobs()
                .toObservable()
                .map { response -> checkResponse(response, context.getString(R.string.error_get_jobs_error)) }
                .map { response -> response.body() ?: emptyList() }
                .map { jobs -> LoadJobsResponse(jobs) }
                .doOnError { error -> Timber.e(error) }
    }

    private fun <T> checkResponse(response: Response<T>, message: String): Response<T> {
        return when {
            response.isSuccessful -> response
            else -> throw IllegalStateException(message)
        }
    }
}
