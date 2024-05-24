package com.example.techknowapp.core.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {
    @GET("pokemon")
    fun getFunction(
//        @QueryMap params: Map<String, String>
    ): Call<DynamicResponse>
}