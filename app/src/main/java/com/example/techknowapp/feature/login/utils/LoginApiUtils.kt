package com.example.techknowapp.feature.login.utils

import android.content.Context
import com.example.techknowapp.core.model.LoginResponse
import com.example.techknowapp.core.rest.ApiClient
import com.example.techknowapp.core.rest.ApiInterface
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LoginApiUtils(private val context: Context, activity: Any) {
    var callback = activity as LoginApiCallback
    fun login(username: String, password: String) {
        val params = HashMap<String, String>()
        params.apply {
            put("username", username)
            put("password", password)
        }

        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.login(params)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Timber.d("success return>>>${Gson().toJson(response.body())}")
                if (response.body() != null) {
                    callback.loginResult(API_SUCCESS, response.body()!!)
                } else {
                    callback.loginResult(API_FAILED, null)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                callback.loginResult(API_FAILED, null)
            }
        })
    }

    companion object {
        const val API_SUCCESS = "success"
        const val API_FAILED = "failed"
    }
}

interface LoginApiCallback {
    fun loginResult(apiResult: String, loginResponse: LoginResponse?)
}