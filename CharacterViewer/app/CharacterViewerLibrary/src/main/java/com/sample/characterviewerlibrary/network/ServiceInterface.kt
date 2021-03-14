package com.sample.characterviewerlibrary.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceInterface {

    @GET("/")
    fun getCharacterList(
        @Query("q") query: String,
        @Query("format") format: String
    ): Call<CharacterListModels.Result>
}