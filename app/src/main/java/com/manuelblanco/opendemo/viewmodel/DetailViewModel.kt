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

class DetailViewModel(private val marvelRepository: MarvelRepository) : ViewModel() {
    private val _character = MutableLiveData<Character>()
    var isNetworkAvailable = MutableLiveData<Boolean>()
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    val character = _character

    fun fetchCharacter(id: Int){
        if (isNetworkAvailable.value!!) {
            CoroutineScope(Dispatchers.Main).launch {
                val resultDetail: Deferred<MarvelResponse> = marvelRepository.getDetailCharacter(id)
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

    private fun verifyResult(result: MarvelResponse){
        when (result.code.toInt()) {
            200 ->  {
                _character.value = result.data.results[0]
                _loadingState.value = LoadingState.SUCCESS
            }
            else -> _loadingState.value = LoadingState.error("Something was wrong")
        }
    }
}