package com.brady.githubjobdemo.data.api.github.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Job(
        @Json(name = "company") val company: String,
        @Json(name = "title") val title: String,
        @Json(name = "location") val location: String,
        @Json(name = "company_url") val company_url : String?,
        @Json(name = "type") val type: String,
        @Json(name = "created_at") val created_at: String,
        @Json(name = "description") val description: String,
        @Json(name = "url") val url: String )


