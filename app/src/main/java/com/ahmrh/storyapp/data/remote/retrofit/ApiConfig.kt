package com.ahmrh.storyapp.data.remote.retrofit

import com.ahmrh.storyapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{

        fun getApiService(): ApiService {

            val authInterceptor = Interceptor{ chain ->
                val req = chain.request()
                val token = ""
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearerr $token")
                    .build()
                chain.proceed(requestHeaders)
            }

            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            val apiUrl = BuildConfig.API_URL

            val retrofit = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}