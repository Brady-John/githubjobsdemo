package com.brady.githubjobdemo.data.api.github

import com.brady.githubjobdemo.data.DataModule
import com.brady.githubjobdemo.data.api.github.model.Job
import com.brady.githubjobdemo.loadResourceAsString
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class GitHubApiServiceTest {

    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        server = MockWebServer()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        server.shutdown()
    }

    @Test
    @Throws(Exception::class)
    fun testListJobsSuccessful() {
        server.enqueue(MockResponse().setBody("/api/listJobs_success.json".loadResourceAsString()))
        server.start()

        val api = buildApi(server)
        val subscriber = TestObserver<Response<List<Job>>>()
        api.listJobs().subscribe(subscriber)
        subscriber.await(1, TimeUnit.SECONDS)

        val serverRequest = server.takeRequest()
        assertEquals("GET", serverRequest.method)
        assertEquals("/repos/test_user/test_repository/jobs", serverRequest.path)

        subscriber.assertNoErrors()
        subscriber.assertComplete()
        subscriber.assertValueCount(1)
        val response = subscriber.values()[0]
        assertTrue(response.isSuccessful)
        val jobs = response.body()
        assertEquals(1, jobs!!.size.toLong())
        val job = jobs[0]
        assertEquals("test company", job.company)
        assertEquals("test title", job.title)
        assertEquals("test location", job.location)
    }

    @Test
    @Throws(Exception::class)
    fun testListJobsUnsuccessful() {
        server.enqueue(MockResponse().setResponseCode(404).setBody("{\"message\": \"Not Found\"}"))
        server.start()

        val api = buildApi(server)
        val subscriber = TestObserver<Response<List<Job>>>()
        api.listJobs().subscribe(subscriber)
        subscriber.await(1, TimeUnit.SECONDS)

        subscriber.assertNoErrors()
        subscriber.assertComplete()
        subscriber.assertValueCount(1)
        val response = subscriber.values()[0]
        assertFalse(response.isSuccessful)
        assertEquals(404, response.code().toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testListJobsNetworkError() {
        val api = buildApi("http://bad_url/")
        val subscriber = TestObserver<Response<List<Job>>>()
        api.listJobs().subscribe(subscriber)
        subscriber.await(1, TimeUnit.SECONDS)

        subscriber.assertNoValues()
        assertEquals(1, subscriber.errors().size.toLong())
        val error = subscriber.errors()[0]
        assertTrue(error is UnknownHostException)
        // Note: You can't compare message text because that will be provided by the underlying runtime
    }

    @Throws(Exception::class)
    private fun buildApi(server: MockWebServer): GitHubApiService {
        val baseUrl = server.url("")
        return buildApi(baseUrl.toString())
    }

    @Throws(Exception::class)
    private fun buildApi(baseUrl: String): GitHubApiService {
        val module = DataModule()
        val client = OkHttpClient.Builder().build()
        val converterFactory = module.provideConverter()
        val retrofit = module.provideRetrofit(client, baseUrl, converterFactory)
        return module.provideGitHubApiService(retrofit)
    }
}