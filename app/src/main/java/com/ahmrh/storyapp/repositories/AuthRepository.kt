package com.ahmrh.storyapp.repositories

import android.util.Log
import com.ahmrh.storyapp.data.remote.responses.DetailStoryResponse
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {
    companion object{
        const val TAG = "AuthRepository"
    }

    private lateinit var token: String
    init{
        val token = ""
    }
    fun login(email: String, password: String) {
    }

    fun login(name: String, email: String, password: String) {
    }
}