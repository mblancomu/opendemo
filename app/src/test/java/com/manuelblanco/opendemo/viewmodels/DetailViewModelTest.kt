package com.manuelblanco.opendemo.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.remote.MarvelResponse
import com.manuelblanco.core.repository.MarvelRepository
import com.manuelblanco.opendemo.utils.CoroutineTestRule
import com.manuelblanco.opendemo.utils.LifeCycleTestOwner
import com.manuelblanco.opendemo.viewmodel.DetailViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class DetailViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val stateObserver: Observer<LoadingState> = mock()
    private val marvelRepository: MarvelRepository = mock()

    private lateinit var lifeCycleTestOwner: LifeCycleTestOwner
    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        lifeCycleTestOwner = LifeCycleTestOwner()
        lifeCycleTestOwner.onCreate()
        detailViewModel = DetailViewModel(marvelRepository)
        detailViewModel.loadingState.observe(lifeCycleTestOwner, stateObserver)
        detailViewModel.isNetworkAvailable.value = true
    }

    @After
    fun tearDown() {
        lifeCycleTestOwner.onDestroy()
    }

    @Test
    fun testForGetACharacterWithNetworkOK() {
        coroutineTestRule.testDispatcher.runBlockingTest {

            lifeCycleTestOwner.onResume()
            val mockResponse = CompletableDeferred<MarvelResponse>()
            When calling marvelRepository.getDetailCharacter(any()) itReturns mockResponse

            detailViewModel.fetchCharacter(any())

            Verify on marvelRepository that marvelRepository.getDetailCharacter(any()) was called
            Verify on stateObserver that stateObserver.onChanged(LoadingState.LOADING) was called
        }
    }

    @Test
    fun testForGetACharacterWithNetworkKO() {
        detailViewModel.isNetworkAvailable.value = false
        lifeCycleTestOwner.onResume()
        detailViewModel.fetchCharacter(any())
        Verify on stateObserver that stateObserver.onChanged(LoadingState.NETWORK) was called
    }
}