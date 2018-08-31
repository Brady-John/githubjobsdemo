package com.brady.githubjobdemo.ui.main

import com.brady.githubjobdemo.TrampolineSchedulerRule
import com.brady.githubjobdemo.data.api.github.GitHubInteractor
import com.brady.githubjobdemo.data.api.github.model.Job
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @JvmField @Rule val trampolineSchedulerRule = TrampolineSchedulerRule()

    @Mock private lateinit var githubInteractor: GitHubInteractor

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val app = RuntimeEnvironment.application
        viewModel = MainViewModel(
                app,
                githubInteractor,
                0)
    }

    @Test
    fun testGetVersion() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "1.0 b[1-9][0-9]*".toRegex()
        assertTrue("1.0 b123".matches(expectedPattern))
        assertTrue(viewModel.getVersion().matches(expectedPattern))
    }

    @Test
    fun testGetFingerprint() {
        // CI systems can change the build number so we are a little more flexible on what to expect
        val expectedPattern = "[a-zA-Z0-9]+".toRegex()
        assertTrue("0569b5cd8".matches(expectedPattern))
        assertTrue(viewModel.getFingerprint().matches(expectedPattern))
    }

    @Test
    fun testFetchJobsEnabled() {
        viewModel.repository = ""
        assertFalse(viewModel.isFetchJobsEnabled())

        viewModel.repository = "test"
        assertFalse(viewModel.isFetchJobsEnabled())

        viewModel.repository = ""
        assertFalse(viewModel.isFetchJobsEnabled())

        viewModel.repository = ""
        assertFalse(viewModel.isFetchJobsEnabled())

        viewModel.repository = "test"
        assertFalse(viewModel.isFetchJobsEnabled())

        viewModel.repository = "test"
        assertTrue(viewModel.isFetchJobsEnabled())
    }

    @Test
    fun testFetchJobs() {
        val mockResult = mock(GitHubInteractor.LoadJobsResponse::class.java)
        val mockJob = mock(Job::class.java)
        whenever(mockResult.jobs).thenReturn(listOf(mockJob))
        whenever(githubInteractor.loadJobs(any())).thenReturn(Observable.just(mockResult))

        assertTrue((viewModel.jobs as? MainViewModel.Jobs.Result)?.jobs?.isEmpty() ?: false)
        viewModel.fetchJobs()
        assertTrue((viewModel.jobs as? MainViewModel.Jobs.Result)?.jobs?.size == 1)
    }
}