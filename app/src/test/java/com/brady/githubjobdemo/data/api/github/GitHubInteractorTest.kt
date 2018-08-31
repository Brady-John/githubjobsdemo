package com.brady.githubjobdemo.data.api.github

import com.brady.githubjobdemo.data.api.github.GitHubInteractor.LoadJobsRequest
import com.brady.githubjobdemo.data.api.github.GitHubInteractor.LoadJobsResponse
import com.brady.githubjobdemo.data.api.github.model.JobTestHelper.stubJob
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import retrofit2.Response
import java.util.Arrays.asList
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class GitHubInteractorTest {

    @Mock lateinit var api: GitHubApiService

    private lateinit var interactor: GitHubInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val context = RuntimeEnvironment.application
        interactor = GitHubInteractor(context, api)
    }

    @Test
    @Throws(Exception::class)
    fun testLoadJobs() {
        val mockResponse = Single.just(Response.success(asList(stubJob("test name", "test message"))))
        whenever(api.listJobs(anyString())).thenReturn(mockResponse)

        val subscriber = TestObserver<LoadJobsResponse>()
        interactor.loadJobs(LoadJobsRequest( "repo")).subscribeWith(subscriber)
        subscriber.await(1, TimeUnit.SECONDS)

        subscriber.assertValueCount(1)
        subscriber.assertNoErrors()
        subscriber.assertComplete()

        val response = subscriber.values()[0]
        assertEquals("repo", response.request.repository)
        assertEquals(1, response.jobs.size.toLong())

        val job = response.jobs[0]
        assertEquals("test name", job.author)
        assertEquals("test message", job.jobMessage)
    }
}