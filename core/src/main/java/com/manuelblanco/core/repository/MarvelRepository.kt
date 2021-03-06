package com.manuelblanco.core.repository

import com.manuelblanco.core.remote.MarvelApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarvelRepository(private val marvelApi: MarvelApi) {

    suspend fun getListCharacters(offset: Int) = withContext(Dispatchers.IO){
            marvelApi.getListCharacters(offset)
    }

    suspend fun getDetailCharacter(id: Int) = withContext(Dispatchers.IO){
            marvelApi.getDetailCharacter(id)
    }
}