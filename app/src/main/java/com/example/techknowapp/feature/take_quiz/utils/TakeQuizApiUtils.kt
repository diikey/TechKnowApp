package com.example.techknowapp.feature.take_quiz.utils

import android.content.Context
import com.example.techknowapp.core.model.TakeQuizRes
import com.example.techknowapp.core.rest.ApiClient
import com.example.techknowapp.core.rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class TakeQuizApiUtils(private val context: Context, activity: Any) {
    var callback = activity as TakeQuizApiCallback

    fun getQuestions(params: HashMap<String, String>) {
        val apiService = ApiClient(context).createService(ApiInterface::class.java)
        val call = apiService.getQuestions(params)
        call.enqueue(object : Callback<TakeQuizRes> {
            override fun onResponse(call: Call<TakeQuizRes>, response: Response<TakeQuizRes>) {
                Timber.d("success quiz>>>${response.body()}")
                if (response.body() != null) {
                    callback.result(API_SUCCESS, response.body())
                } else {
                    callback.result(API_FAILED, null)
                }
            }

            override fun onFailure(call: Call<TakeQuizRes>, t: Throwable) {
                t.printStackTrace()
                callback.result(API_FAILED, null)
            }
        })
    }

    companion object {
        const val API_SUCCESS = "success"
        const val API_FAILED = "failed"
    }
}

interface TakeQuizApiCallback {
    fun <T> result(apiResult: String, response: T?)
}