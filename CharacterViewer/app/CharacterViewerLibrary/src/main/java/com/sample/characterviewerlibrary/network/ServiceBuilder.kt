package com.sample.characterviewerlibrary.network

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.duckduckgo.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }

    fun startCharacterListDownload(characterDownloadInterface: CharacterDownloadInterface,
                                          call : Call<CharacterListModels.Result>) {
        call.enqueue(object : Callback<CharacterListModels.Result> {
            override fun onResponse(call: Call<CharacterListModels.Result>, response: Response<CharacterListModels.Result>) {
                if (response.isSuccessful){
                    CharacterListModels.mTopicsDownloadedList = response.body()!!.RelatedTopics!!
                    characterDownloadInterface.onSuccessResponse(call, response)
                }
            }
            override fun onFailure(call: Call<CharacterListModels.Result>, t: Throwable) {
                characterDownloadInterface.onFailureResponse(call, t)
            }
        })
    }
}