package com.ahmrh.storyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContentProviderCompat.requireContext
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
    companion object{
        const val TAG = "LoginActivity"
    }

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
            Log.d(TAG, "$isLogin")
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

            val authResponseLiveData = authViewModel.auth(email, password)
            authResponseLiveData.observe(this) { authResponse ->
                if (authResponse.success) {
                    Toast.makeText(this, "Authorized User", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
                } else{
                    Toast.makeText(this, authResponse.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }

    }

    private fun setupUI(){
        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}