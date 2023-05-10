package com.ahmrh.storyapp.data.remote.retrofit

import android.util.Log
import com.ahmrh.storyapp.BuildConfig
import com.ahmrh.storyapp.data.local.preferences.AppPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig(private val pref: AppPreferences) {

    fun getApiService(): ApiService {

        val token = pref.getToken()

        Log.d(TAG, "Token : ${token.toString()}")

        val loggingInterceptor = if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY ; HttpLoggingInterceptor.Level.HEADERS
            }
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(token))
            .build()

        val apiUrl = BuildConfig.API_URL

        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }


    class AuthInterceptor(private val token: String?) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            return if(!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "$token")
                chain.proceed(requestBuilder.build())
            } else{
                chain.proceed(chain.request())
            }
        }
    }

    companion object{
        const val TAG = "ApiConfig"
    }

}