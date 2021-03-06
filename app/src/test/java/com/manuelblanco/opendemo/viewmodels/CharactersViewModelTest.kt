package com.manuelblanco.opendemo.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.remote.MarvelResponse
import com.manuelblanco.core.repository.MarvelRepository
import com.manuelblanco.opendemo.utils.CoroutineTestRule
import com.manuelblanco.opendemo.utils.LifeCycleTestOwner
import com.manuelblanco.opendemo.viewmodel.CharactersViewModel
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
class CharactersViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val stateObserver: Observer<LoadingState> = mock()
    private val marvelRepository: MarvelRepository = mock()

    private lateinit var lifeCycleTestOwner: LifeCycleTestOwner
    private lateinit var charactersViewModel: CharactersViewModel

    @Before
    fun setUp() {
        lifeCycleTestOwner = LifeCycleTestOwner()
        lifeCycleTestOwner.onCreate()
        charactersViewModel = CharactersViewModel(marvelRepository)
        charactersViewModel.loadingState.observe(lifeCycleTestOwner, stateObserver)
        charactersViewModel.isNetworkAvailable.value = true
    }

    @After
    fun tearDown() {
        lifeCycleTestOwner.onDestroy()
    }

    @Test
    fun testForGetAListOfCharactersWithNetworkOK() {
        coroutineTestRule.testDispatcher.runBlockingTest {

            lifeCycleTestOwner.onResume()
            val mockResponse = CompletableDeferred<MarvelResponse>()
            When calling marvelRepository.getListCharacters(0) itReturns mockResponse

            charactersViewModel.fetchCharactersData(0)

            Verify on marvelRepository that marvelRepository.getListCharacters(0) was called
            Verify on stateObserver that stateObserver.onChanged(LoadingState.LOADING) was called
        }
    }

    @Test
    fun testForGetAListOfCharactersWithNetworkKO() {
        charactersViewModel.isNetworkAvailable.value = false
        lifeCycleTestOwner.onResume()
        charactersViewModel.fetchCharactersData(0)
        Verify on stateObserver that stateObserver.onChanged(LoadingState.NETWORK) was called
    }
}