package com.sample.characterviewerlibrary.network

import retrofit2.Call
import retrofit2.Response

interface CharacterDownloadInterface {
    fun onSuccessResponse(call: Call<CharacterListModels.Result>, response: Response<CharacterListModels.Result>)
    fun onFailureResponse(call: Call<CharacterListModels.Result>, t: Throwable)
}