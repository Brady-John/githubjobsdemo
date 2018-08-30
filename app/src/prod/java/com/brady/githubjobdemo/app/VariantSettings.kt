package com.brady.githubjobdemo.app

import android.content.Context
import com.brady.githubjobdemo.R

open class VariantSettings(private val context: Context) {
    val baseUrl: String = context.getString(R.string.default_base_url)
}
