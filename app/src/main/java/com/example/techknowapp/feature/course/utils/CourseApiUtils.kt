package com.example.techknowapp.feature.course.utils

import android.content.Context
import com.example.techknowapp.core.model.Announcement
import com.example.techknowapp.core.model.CourseModule
import com.example.techknowapp.core.rest.ApiClient
import com.example.techknowapp.core.rest.ApiInterface
import com.example.techknowapp.core.rest.DynamicResponse
import com.example.techknowapp.core.utils.Cache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class CourseApiUtils(private val context: Context, activity: Any) {
    var callback = activity as CourseApiCallback
    private val cache = Cache(context)

    fun getCourseAnnouncements(params: HashMap<String, String>) {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getCourseAnnouncement(params)
        call.enqueue(object : Callback<List<Announcement>> {
            override fun onResponse(
                call: Call<List<Announcement>>,
                response: Response<List<Announcement>>
            ) {
                Timber.d("announcements>>>${response.body()}")
            }

            override fun onFailure(call: Call<List<Announcement>>, t: Throwable) {
                Timber.d("error announcements>>>")
                t.printStackTrace()
            }
        })
    }

    fun getCourseModules(params: HashMap<String, String>) {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getCourseModules(params)
        call.enqueue(object : Callback<List<CourseModule>> {
            override fun onResponse(
                call: Call<List<CourseModule>>,
                response: Response<List<CourseModule>>
            ) {
                Timber.d("success modules>>>${response.body()}")
                if (response.body() != null) {
                    callback.result(API_SUCCESS, response.body())
                } else {
                    callback.result(API_FAILED, null)
                }
            }

            override fun onFailure(call: Call<List<CourseModule>>, t: Throwable) {
                t.printStackTrace()
                callback.result(API_FAILED, null)
            }
        })
    }

    fun getCourseQuizzes(params: HashMap<String, String>) {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getQuiz(params)
        call.enqueue(object : Callback<List<DynamicResponse>> {
            override fun onResponse(
                call: Call<List<DynamicResponse>>,
                response: Response<List<DynamicResponse>>
            ) {
                Timber.d("modules>>>${response.body()}")
            }

            override fun onFailure(call: Call<List<DynamicResponse>>, t: Throwable) {
                Timber.d("error modules>>>")
                t.printStackTrace()
            }
        })
    }

    companion object {
        const val API_SUCCESS = "success"
        const val API_FAILED = "failed"
    }
}

interface CourseApiCallback {
    fun <T> result(apiResult: String, response: T?)
}