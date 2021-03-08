package com.manuelblanco.opendemo.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.repository.MarvelRepository
import com.manuelblanco.opendemo.utils.CoroutineTestRule
import com.manuelblanco.opendemo.utils.LifeCycleTestOwner
import com.manuelblanco.opendemo.viewmodel.CharactersViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.mock
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
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
    }

    @After
    fun tearDown() {
        lifeCycleTestOwner.onDestroy()
    }

    @Test
    fun testForGetAListOfCharacters() {
        coroutineTestRule.testDispatcher.runBlockingTest {
            // Given
            lifeCycleTestOwner.onResume()
            //val responseCharacters: Deferred<MarvelResponse> =
//            When calling marvelRepository.getListCharacters(0) itReturns responseCharacters
//            // When
//            postViewModel.getRedditPost()
//            // Then
//            Verify on redditRepository that redditRepository.getPost() was called
//            Verify on stateObserver that stateObserver.onChanged(State.Loading) was called
        }
    }

}