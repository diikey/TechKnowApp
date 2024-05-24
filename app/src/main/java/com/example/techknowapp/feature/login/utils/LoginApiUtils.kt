package com.example.techknowapp.feature.login.utils

import android.content.Context
import com.example.techknowapp.core.rest.ApiClient
import com.example.techknowapp.core.rest.ApiInterface
import com.example.techknowapp.core.rest.DynamicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginApiUtils(private val context: Context) {
    fun callApi() {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getFunction()
        call.enqueue(object : Callback<DynamicResponse> {
            override fun onResponse(
                call: Call<DynamicResponse>,
                response: Response<DynamicResponse>
            ) {
//                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<DynamicResponse>, t: Throwable) {
//                TODO("Not yet implemented")
            }
        })
    }
}