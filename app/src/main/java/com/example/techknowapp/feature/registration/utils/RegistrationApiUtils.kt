package com.example.techknowapp.feature.registration.utils

import android.content.Context
import com.example.techknowapp.core.model.LoginResponse
import com.example.techknowapp.core.rest.ApiClient
import com.example.techknowapp.core.rest.ApiInterface
import com.example.techknowapp.feature.login.utils.LoginApiUtils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RegistrationApiUtils(private val context: Context, activity: Any) {
    var callback = activity as RegistrationApiCallback

    fun register(params: HashMap<String, String>) {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.register(params)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Timber.d("success return>>>${Gson().toJson(response.body())}")
                if (response.body() != null) {
                    callback.registerResult(LoginApiUtils.API_SUCCESS)
                } else {
                    callback.registerResult(LoginApiUtils.API_FAILED)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                callback.registerResult(API_FAILED)
            }
        })
    }

    companion object {
        const val API_SUCCESS = "success"
        const val API_FAILED = "failed"
    }
}

interface RegistrationApiCallback {
    fun registerResult(apiResult: String)
}