package com.ahmrh.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.*
import com.ahmrh.storyapp.data.local.AppPreferences
import com.ahmrh.storyapp.data.local.Login
import com.ahmrh.storyapp.data.remote.responses.DefaultResponse
import com.ahmrh.storyapp.data.remote.responses.LoginResponse
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import com.ahmrh.storyapp.repositories.AuthRepository
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

    private fun savePrefLogin() {
        viewModelScope.launch{
            login.value?.let { pref.saveLogin(it) }
        }
    }

    fun auth(email: String, password: String) {

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val name = response.body()?.loginResult?.name.toString()
                    val userId = response.body()?.loginResult?.userId.toString()
                    val token = response.body()?.loginResult?.token.toString()
                    _login.value = Login(name, userId, token)

                    savePrefLogin()
                } else {
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailureThrowable: ${t.message}")
            }
        })

    }

    fun register(name: String, email: String, password: String){
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {

                } else {
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.e(TAG, "onFailureThrowable: ${t.message}")
            }
        })
    }

    fun getName(): LiveData<String> {
        return pref.getName().asLiveData()
    }
    fun isLogin(): LiveData<Boolean> {
        return pref.isLogin().asLiveData()
    }
}