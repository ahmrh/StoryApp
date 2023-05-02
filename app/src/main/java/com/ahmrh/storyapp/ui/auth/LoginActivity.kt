package com.ahmrh.storyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ahmrh.storyapp.data.local.AppPreferences
import com.ahmrh.storyapp.databinding.ActivityLoginBinding
import com.ahmrh.storyapp.ui.main.MainActivity
import com.ahmrh.storyapp.util.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUtil()
        setupUI()
        setupAction()

        loginCheck()
    }

    private fun setupUtil() {
        val pref = AppPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )
    }

    private fun loginCheck() {
        authViewModel.isLogin().observe(this) { isLogin ->
            if (isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnSubmit.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = "Masih kosong"
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = "Masih kosong"
                }
                else -> {
                    val success = authViewModel.auth(email, password)

                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
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