package com.example.techknowapp.core.rest

import com.example.techknowapp.core.model.Course
import com.example.techknowapp.core.model.GlobalAnnouncement
import com.example.techknowapp.core.model.LoginResponse
import com.example.techknowapp.core.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {
    /**
     * GET REQUEST
     */
    @GET("get_course")
    fun getCourse(): Call<List<Course>>

    @GET("profile")
    fun getProfile(): Call<User>

    @GET("get_global_announcement")
    fun getGlobalAnnouncement(): Call<List<GlobalAnnouncement>>

    @GET("get_course_announcement")
    fun getCourseAnnouncement(@QueryMap params: Map<String, String>): Call<List<DynamicResponse>>

    @GET("get_course_module")
    fun getCourseModules(@QueryMap params: Map<String, String>): Call<List<DynamicResponse>>

    /**
     * POST REQUEST
     */
    @POST("login")
    fun login(@Body params: HashMap<String, String>): Call<LoginResponse>

    @POST("register")
    fun register(@Body params: Map<String, String>): Call<LoginResponse>

    @POST("applycourse")
    fun applyCourse(@Body params: Map<String, String>): Call<DynamicResponse>

    @POST("profile")
    fun postProfile(@Body params: Map<String, String>): Call<DynamicResponse>
}