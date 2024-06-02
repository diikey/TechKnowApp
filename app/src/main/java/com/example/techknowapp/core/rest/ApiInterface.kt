package com.example.techknowapp.core.rest

import com.example.techknowapp.core.model.Course
import com.example.techknowapp.core.model.Announcement
import com.example.techknowapp.core.model.CourseModule
import com.example.techknowapp.core.model.LoginResponse
import com.example.techknowapp.core.model.Quiz
import com.example.techknowapp.core.model.TakeQuizRes
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
    fun getGlobalAnnouncement(): Call<List<Announcement>>

    @GET("course_announcement")
    fun getCourseAnnouncement(@QueryMap params: Map<String, String>): Call<List<Announcement>>

    @GET("course_module")
    fun getCourseModules(@QueryMap params: Map<String, String>): Call<List<CourseModule>>

    @GET("get_quiz")
    fun getQuiz(@QueryMap params: Map<String, String>): Call<List<Quiz>>

    @GET("get_multiple_choice")
    fun getQuestions(@QueryMap params: Map<String, String>): Call<TakeQuizRes>

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