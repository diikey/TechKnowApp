package com.example.techknowapp.feature.dashboard.utils

import android.content.Context
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.core.model.GlobalAnnouncement
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

    fun getAnnouncements() {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getGlobalAnnouncement()
        call.enqueue(object : Callback<List<GlobalAnnouncement>> {
            override fun onResponse(
                call: Call<List<GlobalAnnouncement>>,
                response: Response<List<GlobalAnnouncement>>
            ) {
                Timber.d("announcements>>>${response.body()}")
                if (response.body() != null) {
                    callback.result(ANNOUNCEMENT_SUCCESS, response.body())
                } else {
                    callback.result(ANNOUNCEMENT_FAILED, null)
                }
            }

            override fun onFailure(call: Call<List<GlobalAnnouncement>>, t: Throwable) {
                t.printStackTrace()
                callback.result(ANNOUNCEMENT_FAILED, null)
            }
        })
    }

    fun getCourse() {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getCourse()
        call.enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                Timber.d("success result>>>${Gson().toJson(response.body())}")
                if (response.body() != null) {
                    callback.result(API_SUCCESS, response.body())
                } else {
                    callback.result(API_FAILED, null)
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                t.printStackTrace()
                callback.result(API_FAILED, null)
            }
        })
    }

    fun applyCourse(params: HashMap<String, String>) {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.applyCourse(params)
        call.enqueue(object : Callback<DynamicResponse> {
            override fun onResponse(
                call: Call<DynamicResponse>,
                response: Response<DynamicResponse>
            ) {
                Timber.d("success result>>>${response.body()}")
                if (response.body() != null) {
                    callback.result(APPLICATION_SUCCESS, null)
                } else {
                    callback.result(APPLICATION_FAILED, null)
                }
            }

            override fun onFailure(call: Call<DynamicResponse>, t: Throwable) {
                t.printStackTrace()
                callback.result(APPLICATION_FAILED, null)
            }
        })
    }

    companion object {
        const val API_SUCCESS = "success"
        const val API_FAILED = "failed"
        const val APPLICATION_SUCCESS = "application_success"
        const val APPLICATION_FAILED = "application_failed"
        const val ANNOUNCEMENT_SUCCESS = "announcement_success"
        const val ANNOUNCEMENT_FAILED = "announcement_failed"
    }
}

interface DashboardApiCallback {
    fun <T> result(apiResult: String, response: T?)
}