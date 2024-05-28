package com.example.techknowapp.feature.dashboard.utils

import android.content.Context
import com.example.techknowapp.core.rest.ApiClient
import com.example.techknowapp.core.rest.ApiInterface
import com.example.techknowapp.core.rest.DynamicResponse
import com.example.techknowapp.core.utils.Cache
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DashboardApiUtils(private val context: Context, activity: Any) {
    var callback = activity as DashboardApiCallback
    private val cache = Cache(context)

    fun getCourse() {
        val params = HashMap<String, String>().apply {
            put("token", cache.getString(Cache.TOKEN, "")!!)
        }
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getCourse(params)
        call.enqueue(object : Callback<DynamicResponse> {
            override fun onResponse(
                call: Call<DynamicResponse>,
                response: Response<DynamicResponse>
            ) {
                Timber.d("success result>>>${Gson().toJson(response.body())}")
                if (response.body() != null) {
                    callback.result(API_SUCCESS)
                } else {
                    callback.result(API_FAILED)
                }
            }

            override fun onFailure(call: Call<DynamicResponse>, t: Throwable) {
                t.printStackTrace()
                callback.result(API_FAILED)
            }
        })
    }

    companion object {
        const val API_SUCCESS = "success"
        const val API_FAILED = "failed"
    }
}

interface DashboardApiCallback {
    fun result(apiResult: String)
}