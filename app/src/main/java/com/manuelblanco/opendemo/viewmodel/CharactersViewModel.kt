package com.manuelblanco.opendemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.model.Character
import com.manuelblanco.core.remote.MarvelResponse
import com.manuelblanco.core.repository.MarvelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * View Model in charge of calling the Characters. It is done through a coroutine, where the
 * State is updated and the obtained code is verified. The characterList variable is updated
 * with the values, for later use in the fragment that needs it.
 */
class CharactersViewModel(private val marvelRepository: MarvelRepository) : ViewModel() {
    private val _characterList = MutableLiveData<List<Character>>()
    var isNetworkAvailable = MutableLiveData<Boolean>()
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    val characterList = _characterList

    /**
     * Fetch data for list of Characters. Network status is checked before the call
     */
    fun fetchCharactersData(offset: Int = 0){
        if (isNetworkAvailable.value!!) {
            CoroutineScope(Dispatchers.Main).launch {
                val resultDetail: Deferred<MarvelResponse> = marvelRepository.getListCharacters(offset)
                try {
                    _loadingState.value = LoadingState.LOADING
                    val result = resultDetail.await()
                    verifyResult(result)
                } catch (e: Exception) {
                    _loadingState.value = LoadingState.error(e.message)
                }
            }
        } else {
            _loadingState.value = LoadingState.NETWORK
        }
    }

    /**
     * The obtained code is verified. In the case of wanting to control the different codes, you could
     * create a method with them, or an exception package with the different errors.
     */
    private fun verifyResult(result: MarvelResponse){
        when (result.code.toInt()) {
            200 ->  {
                _characterList.value = result.data.results
                _loadingState.value = LoadingState.SUCCESS
            }
            else -> _loadingState.value = LoadingState.error("Something was wrong")
        }
    }

    fun updateLoadingState(state: LoadingState){
        _loadingState.value = state
    }
}