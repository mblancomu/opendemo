package com.manuelblanco.core.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Calls for Marvel API using retrofit. Using Deferred for coroutines.
 */
interface MarvelApi {
    @GET("/v1/public/characters")
    fun getListCharacters(@Query("offset") offset: Int): Deferred<MarvelResponse>

    @GET("/v1/public/characters/{characterId}")
    fun getDetailCharacter(@Path("characterId") characterId: Int): Deferred<MarvelResponse>
}