package com.brady.githubjobdemo.ui.main

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.brady.githubjobdemo.EspressoMatchers.regex
import com.brady.githubjobdemo.MainApplicationDaggerMockRule
import com.brady.githubjobdemo.R
import com.brady.githubjobdemo.data.api.github.GitHubInteractor
import com.brady.githubjobdemo.data.api.github.GitHubInteractor.LoadJobsResponse
import com.brady.githubjobdemo.data.api.github.model.Job
import com.brady.githubjobdemo.withRecyclerView
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    @JvmField @Rule var mockitoRule = MainApplicationDaggerMockRule()

    @JvmField @Rule var testRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Mock lateinit var gitHubInteractor: GitHubInteractor

    @Test
    fun testBuildFingerprint() {
        whenever(gitHubInteractor.loadJobs()).thenReturn(Observable.empty())

        testRule.launchActivity(null)
        onView(withId(R.id.fingerprint)).check(matches(withText(regex("Fingerprint: .+"))))
    }

    @Test
    fun testFetchJobsEnabledState() {
        val response = LoadJobsResponse(
                emptyList())
        whenever(gitHubInteractor.loadJobs()).thenReturn(Observable.just(response))

        testRule.launchActivity(null)
        onView(withId(R.id.get_jobs)).check(matches(isEnabled()))

        //onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.get_jobs)).check(matches(not(isEnabled())))

        //onView(withId(R.id.username)).perform(typeText("username"))
        onView(withId(R.id.get_jobs)).check(matches(isEnabled()))

        //onView(withId(R.id.repository)).perform(clearText())
        onView(withId(R.id.get_jobs)).check(matches(not(isEnabled())))
    }

    @Test
    fun testGetAndDisplayJobs() {
        val response = buildMockLoadJobsResponse()
        whenever(gitHubInteractor.loadJobs()).thenReturn(response)

        testRule.launchActivity(null)
        closeSoftKeyboard()

        onView(withRecyclerView(R.id.jobs)
                .atPositionOnView(0, R.id.job_title))
                .check(matches(withText("Author: Test author")))
        onView(withRecyclerView(R.id.jobs)
                .atPositionOnView(0, R.id.company))
                .check(matches(withText("Test job message")))
    }

    private fun buildMockLoadJobsResponse(): Observable<LoadJobsResponse> {
        val job = Job("Atomic Robot", "Senior Engineer", "Mason, OH",
                "https://atomicrobot.com/", "Full Time",
                "Thu August 30 19:46:36 UTC 2018", "Awesome Job",
                "https://google.com")
        return Observable.just(LoadJobsResponse(listOf(job)))
    }
}
