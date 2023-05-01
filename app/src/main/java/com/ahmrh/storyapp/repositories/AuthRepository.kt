package com.ahmrh.storyapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmrh.storyapp.data.remote.responses.LoginResponse
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepository {
    companion object{
        const val TAG = "AuthRepository"
    }
    fun login(name: String, email: String, password: String) {
    }
}