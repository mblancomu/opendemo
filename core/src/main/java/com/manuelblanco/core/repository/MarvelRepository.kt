package com.manuelblanco.core.repository

import com.manuelblanco.core.remote.MarvelApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for Characters calls with suspend methods for coroutines.
 */
class MarvelRepository(private val marvelApi: MarvelApi) {

    suspend fun getListCharacters(offset: Int) = withContext(Dispatchers.IO){
            marvelApi.getListCharacters(offset)
    }

    suspend fun getDetailCharacter(id: Int) = withContext(Dispatchers.IO){
            marvelApi.getDetailCharacter(id)
    }
}