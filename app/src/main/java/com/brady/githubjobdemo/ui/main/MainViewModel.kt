package com.brady.githubjobdemo.ui.main

import android.app.Application
import android.databinding.Bindable
import android.os.Parcelable
import android.support.annotation.VisibleForTesting
import com.brady.githubjobdemo.BR
import com.brady.githubjobdemo.BuildConfig
import com.brady.githubjobdemo.R
import com.brady.githubjobdemo.data.api.github.GitHubInteractor
import com.brady.githubjobdemo.data.api.github.model.Job
import com.brady.githubjobdemo.ui.BaseViewModel
import com.brady.githubjobdemo.ui.SimpleSnackbarMessage
import com.brady.githubjobdemo.util.RxUtils.delayAtLeast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
        private val app: Application,
        private val gitHubInteractor: GitHubInteractor,
        @Named("loading_delay_ms") private val loadingDelayMs: Long)
    : BaseViewModel<MainViewModel.State>(app, STATE_KEY, State()) {

    @Parcelize
    class State(
            var repository: String = "") : Parcelable

    sealed class Jobs {
        class Loading : Jobs()
        class Result(val jobs: List<Job>) : Jobs()
        class Error(val message: String) : Jobs()
    }

    override fun setupViewModel() {
        repository = "android-starter-project"  // NON-NLS

        fetchJobs()
    }

    @VisibleForTesting
    internal var jobs: Jobs = Jobs.Result(emptyList())
        set(value) {
            field = value

            notifyPropertyChanged(BR.loading)
            notifyPropertyChanged(BR.jobs)
            notifyPropertyChanged(BR.fetchJobsEnabled)

            when (value) {
                is Jobs.Error -> snackbarMessage.value = value.message
            }
        }

    val snackbarMessage = SimpleSnackbarMessage()

     var repository: String
        @Bindable get() = state.repository
        set(value) {
            state.repository = value
            notifyPropertyChanged(BR.repository)
        }

    @Bindable("repository")
    fun isFetchJobsEnabled(): Boolean = jobs !is Jobs.Loading && !repository.isEmpty()

    @Bindable
    fun isLoading(): Boolean = jobs is Jobs.Loading

    @Bindable
    fun getJobs() = jobs.let {
        when (it) {
            is Jobs.Result -> it.jobs
            else -> emptyList()
        }
    }

    fun getVersion(): String = BuildConfig.VERSION_NAME

    fun getFingerprint(): String = BuildConfig.VERSION_FINGERPRINT

    fun fetchJobs() {
        jobs = Jobs.Loading()
        disposables.add(delayAtLeast(gitHubInteractor.loadJobs(), loadingDelayMs)
                .map { it.jobs }  // Pull the jobs out of the response
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { jobs = Jobs.Result(it) },
                        { jobs = Jobs.Error(it.message ?: app.getString(R.string.error_unexpected)) }))
    }

    companion object {
        private const val STATE_KEY = "MainViewModelState"  // NON-NLS
    }
}
