package com.ahmrh.storyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.data.local.AppPreferences
import com.ahmrh.storyapp.databinding.ActivityRegisterBinding
import com.ahmrh.storyapp.util.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUtil()
        setupUI()
        setupAction()
    }

    private fun setupUtil() {
        val pref = AppPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )
    }
    private fun setupAction() {
        binding.btnSubmit.setOnClickListener{
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            val authResponseLiveData = authViewModel.register(name, email, password)
            authResponseLiveData.observe(this){authResponse ->
                if(authResponse.success){
                    Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                    finish()
                } else{
                    Toast.makeText(this, authResponse.message, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun setupUI(){
        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}