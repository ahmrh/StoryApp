package com.ahmrh.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.ahmrh.storyapp.databinding.ActivityLoginBinding
import com.ahmrh.storyapp.ui.main.MainActivity
import com.ahmrh.storyapp.util.ViewModelFactory

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

    override fun onBackPressed() {
        authViewModel.isLogin.observe(this){isLogin ->
            if(!isLogin) {
                finishAffinity()
                return@observe
            }

            super.onBackPressed()
        }
    }

    private fun setupUtil() {
        authViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(
            AuthViewModel::class.java
        )
    }

    private fun loginCheck() {
        authViewModel.isLogin.observe(this) { isLogin ->
            Log.d(TAG, "$isLogin")
            if (isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnSubmit.setOnClickListener {
            if(binding.edLoginEmail.error != null || binding.edLoginPassword.error != null){
                Toast.makeText(this, "Please recheck above input error", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

        supportActionBar?.hide()
    }


    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}