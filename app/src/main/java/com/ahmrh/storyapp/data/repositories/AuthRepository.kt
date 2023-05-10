package com.ahmrh.storyapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmrh.storyapp.data.local.preferences.AppPreferences
import com.ahmrh.storyapp.data.local.preferences.Login
import com.ahmrh.storyapp.data.remote.responses.DefaultResponse
import com.ahmrh.storyapp.data.remote.responses.LoginResponse
import com.ahmrh.storyapp.data.remote.retrofit.ApiService
import com.ahmrh.storyapp.ui.auth.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepository(private val apiService : ApiService, private val pref: AppPreferences) {
    fun getName() = pref.getName()
    fun isLogin() = pref.isLogin()

    suspend fun clearSession() = pref.deleteLogin()

    fun auth(email: String, password: String, viewModelScope: CoroutineScope): LiveData<AuthResponse> {
        val authResponseLiveData = MutableLiveData<AuthResponse>()
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    authResponseLiveData.value = AuthResponse(
                        response.isSuccessful,
                        response.body()?.message ?: "Authorized User"
                    )
                    val name = response.body()?.loginResult?.name.toString()
                    val userId = response.body()?.loginResult?.userId.toString()
                    val token = response.body()?.loginResult?.token.toString()

                    viewModelScope.launch{
                        pref.saveLogin(Login(name, userId, token))
                    }

                } else {
                    authResponseLiveData.value =
                        AuthResponse(response.isSuccessful, "Incorrect input data")
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                authResponseLiveData.value =
                    AuthResponse(false, "Please check your input")
                Log.e(TAG, "onFailureThrowable: ${t.message}")
            }
        })

        return authResponseLiveData
    }

    fun register(name: String, email: String, password: String) : LiveData<AuthResponse>{
        val authResponseLiveData = MutableLiveData<AuthResponse>()
        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.e(AuthViewModel.TAG, response.body().toString())
                if (response.isSuccessful) {
                    authResponseLiveData.value = AuthResponse(
                        response.isSuccessful,
                        response.body()?.message ?: "User registered"
                    )

                } else {
                    authResponseLiveData.value =
                        AuthResponse(response.isSuccessful, "Incorrect input data")
                    Log.e(AuthViewModel.TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                authResponseLiveData.value =
                    AuthResponse(false, "Please check your input")
                Log.e(AuthViewModel.TAG, "onFailureThrowable: ${t.message}")
            }
        })

        return authResponseLiveData
    }

    data class AuthResponse(
        val success : Boolean,
        val message: String,
    )
    companion object {
        const val TAG = "AuthRepository"
    }
}