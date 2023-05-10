package com.ahmrh.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.*
import com.ahmrh.storyapp.data.repositories.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel(){
    companion object{
        const val TAG = "AuthViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _name = authRepository.getName()
    val name: String = _name ?: ""

    private val _isLogin = authRepository.isLogin().asLiveData()
    val isLogin: LiveData<Boolean> = _isLogin


    fun auth(email: String, password: String): LiveData<AuthRepository.AuthResponse> {
        _isLoading.value = true
        val response = authRepository.auth(email, password, viewModelScope)

        response.observeForever(object : Observer<AuthRepository.AuthResponse> {
            override fun onChanged(value: AuthRepository.AuthResponse) {
                value.let {
                    _isLoading.value = false
                    response.removeObserver(this)
                }
            }
        })

        return response
    }

    fun register(name: String, email: String, password: String): LiveData<AuthRepository.AuthResponse> {
        _isLoading.value = true
        val response = authRepository.register(name, email, password)
        response.observeForever(object : Observer<AuthRepository.AuthResponse> {
            override fun onChanged(value: AuthRepository.AuthResponse) {
                value.let {
                    _isLoading.value = false
                    response.removeObserver(this)
                }
            }
        })

        return response
    }

    suspend fun endSession() = authRepository.clearSession()

}