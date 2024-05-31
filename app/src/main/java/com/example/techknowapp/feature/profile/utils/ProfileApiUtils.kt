package com.example.techknowapp.feature.profile.utils

import android.content.Context
import com.example.techknowapp.core.model.User
import com.example.techknowapp.core.rest.ApiClient
import com.example.techknowapp.core.rest.ApiInterface
import com.example.techknowapp.core.rest.DynamicResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ProfileApiUtils(private val context: Context, activity: Any){
    var callback = activity as ProfileApiCallback

    fun getProfile(){
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getProfile()
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Timber.d("success result>>>${Gson().toJson(response.body())}")
                if (response.body() != null) {
                    callback.result(API_SUCCESS, response.body())
                } else {
                    callback.result(API_FAILED, null)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
                callback.result(API_FAILED, null)
            }
        })
    }

    fun saveProfile(params: HashMap<String,String>){

        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.postProfile(params)
        call.enqueue(object : Callback<DynamicResponse>{
            override fun onResponse(
                call: Call<DynamicResponse>,
                response: Response<DynamicResponse>
            ) {
                Timber.d("Test result save " + Gson().toJson(response.body()))
                if (response.body() != null) {
                    callback.result(POST_SUCCESS, response.body())
                } else {
                    callback.result(POST_FAILED, null)
                }
            }

            override fun onFailure(call: Call<DynamicResponse>, t: Throwable) {
                t.printStackTrace()
                callback.result(POST_FAILED,null)
            }

        })
    }



    companion object {
        const val API_SUCCESS = "success"
        const val API_FAILED = "failed"
        const val POST_SUCCESS = "post_success"
        const val POST_FAILED = "post_failed"
    }
}

interface ProfileApiCallback {
    fun <T> result(apiResult: String, response: T?)
}