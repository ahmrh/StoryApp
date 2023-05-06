package com.ahmrh.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.*
import com.ahmrh.storyapp.data.local.AppPreferences
import com.ahmrh.storyapp.data.local.Login
import com.ahmrh.storyapp.data.remote.responses.DefaultResponse
import com.ahmrh.storyapp.data.remote.responses.LoginResponse
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: AppPreferences) : ViewModel(){
    companion object{
        const val TAG = "AuthViewModel"
    }

    private val _login = MutableLiveData<Login>()
    val login: LiveData<Login> = _login

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun isLogin(): LiveData<Boolean>  = pref.isLogin().asLiveData()

    private fun savePrefLogin() {
        _isLoading.value = true
        viewModelScope.launch{
            login.value?.let { pref.saveLogin(it) }
        }
        _isLoading.value = false
    }

    private fun deletePrefLogin() {
        _isLoading.value = true
        viewModelScope.launch {
            pref.deleteLogin()
        }
        _isLoading.value = false
    }

    fun auth(email: String, password: String): LiveData<AuthResponse> {
        _isLoading.value = true
        val authResponseLiveData = MutableLiveData<AuthResponse>()
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {

                if (response.isSuccessful) {
                    authResponseLiveData.value = AuthResponse(response.isSuccessful,
                        response.body()?.message ?: "Authorized User"
                    )
                    _isLoading.value = false
                    val name = response.body()?.loginResult?.name.toString()
                    val userId = response.body()?.loginResult?.userId.toString()
                    val token = response.body()?.loginResult?.token.toString()
                    _login.value = Login(name, userId, token)

                    savePrefLogin()
                } else {
                    authResponseLiveData.value = AuthResponse(response.isSuccessful, "Incorrect input data")
                    _isLoading.value = false
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                authResponseLiveData.value = AuthResponse(false, "Please check your input")
                Log.e(TAG, "onFailureThrowable: ${t.message}")
            }
        })

        return authResponseLiveData

    }

    fun register(name: String, email: String, password: String): LiveData<AuthResponse> {
        _isLoading.value = true
        val authResponseLiveData = MutableLiveData<AuthResponse>()
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.e(TAG, response.body().toString())
                if (response.isSuccessful) {
                    authResponseLiveData.value = AuthResponse(response.isSuccessful,
                        response.body()?.message ?: "User registered"
                    )
                    _isLoading.value = false
                } else {
                    authResponseLiveData.value = AuthResponse(response.isSuccessful, "Incorrect input data")
                    _isLoading.value = false
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                authResponseLiveData.value = AuthResponse(false, "Please check your input")
                _isLoading.value = false
                Log.e(TAG, "onFailureThrowable: ${t.message}")
            }
        })

        return authResponseLiveData
    }

    data class AuthResponse(
        val success : Boolean,
        val message: String,
    )

    fun endSession(){
        deletePrefLogin()
    }

}