package com.example.techknowapp.core.rest

import android.content.Context
import com.example.techknowapp.core.utils.Constants.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val timeoutRead = 120   //In seconds
private const val contentType = "Content-Type"
private const val token = "token"
private const val contentTypeValue = "application/json"
private const val timeoutConnect = 120   //In seconds

class ApiClient(context: Context) {
    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

    private var retrofit: Retrofit
//    private val cache = Cache(context)

    private var headerInterceptor = Interceptor { chain ->
        val original = chain.request()

        val request = original.newBuilder()
            .header(contentType, contentTypeValue)
            .method(original.method, original.body)
//        if(cache.getString(Cache.CACHE_TOKEN,"") != ""){
//            request.addHeader("token",cache.getString(Cache.CACHE_TOKEN))
//            request.addHeader("mobile", "1")
//        }

        Timber.d(">>>>SERVICEGENERATOR >>> headerInterceptor")
        chain.proceed(request.build())
    }

    private val logger: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
//            if (BuildConfig.DEBUG) {
//                loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
//            }
            return loggingInterceptor
        }

    init {
        okHttpBuilder.addInterceptor(headerInterceptor)
        okHttpBuilder.addInterceptor(logger)
        okHttpBuilder.connectTimeout(timeoutConnect.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(timeoutRead.toLong(), TimeUnit.SECONDS)
        val client = okHttpBuilder.build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Timber.d(">>>>SERVICEGENERATOR >>> INITIALIZED BASE URL $BASE_URL")
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}
