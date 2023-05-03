package com.ahmrh.storyapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.data.local.AppPreferences
import com.ahmrh.storyapp.databinding.ActivityMainBinding
import com.ahmrh.storyapp.ui.auth.AuthViewModel
import com.ahmrh.storyapp.ui.auth.LoginActivity
import com.ahmrh.storyapp.ui.story.DetailStoryFragment
import com.ahmrh.storyapp.ui.story.ListStoryFragment
import com.ahmrh.storyapp.util.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")
class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel
    internal lateinit var mainViewModel: MainViewModel

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mListStoryFragment: ListStoryFragment
    private lateinit var mDetailStoryFragment: DetailStoryFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUtil()

    }

    private fun setupUtil() {
        val pref = AppPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )
        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mFragmentManager = supportFragmentManager
        mListStoryFragment = ListStoryFragment()
        mDetailStoryFragment = DetailStoryFragment()

        val fragment = mFragmentManager.findFragmentByTag(ListStoryFragment::class.java.simpleName)
        if (fragment !is ListStoryFragment) {
            Log.d(TAG, "Fragment Name :" + ListStoryFragment::class.java.simpleName)
            this.mFragmentManager.commit {
                add(R.id.frame_container, mListStoryFragment, ListStoryFragment::class.java.simpleName)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout -> {
                authViewModel.endSession()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        }

        return true
    }
}