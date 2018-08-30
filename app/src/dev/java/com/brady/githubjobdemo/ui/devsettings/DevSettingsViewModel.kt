package com.brady.githubjobdemo.ui.devsettings

import android.app.Application
import android.databinding.Bindable
import android.os.Parcelable
import com.brady.githubjobdemo.BR
import com.brady.githubjobdemo.app.Settings
import com.brady.githubjobdemo.ui.BaseViewModel
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

class DevSettingsViewModel @Inject constructor(
        private val app: Application,
        private val settings: Settings)
    : BaseViewModel<DevSettingsViewModel.State>(app, STATE_KEY, State()) {

    @Parcelize
    data class State(
            var baseUrl: String = "",
            var trustAllSSL: Boolean = false) : Parcelable

    override fun setupViewModel() {
        baseUrl = settings.baseUrl
        trustAllSSL = settings.trustAllSSL
    }

    var baseUrl: String
        @Bindable get() = state.baseUrl
        set(value) {
            state.baseUrl = value
            notifyPropertyChanged(BR.baseUrl)
        }

    var trustAllSSL: Boolean
        @Bindable get() = state.trustAllSSL
        set(value) {
            state.trustAllSSL = value
            notifyPropertyChanged(BR.trustAllSSL)
        }

    fun saveSettings() {
        settings.baseUrl = state.baseUrl
        settings.trustAllSSL = state.trustAllSSL
    }

    companion object {
        private const val STATE_KEY = "DevSettingsViewModelState"  // NON-NLS
    }
}
