package com.ahmrh.storyapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.ahmrh.storyapp.data.local.preferences.AppPreferences
import com.ahmrh.storyapp.data.local.database.StoryDatabase
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import com.ahmrh.storyapp.data.repositories.AuthRepository
import com.ahmrh.storyapp.data.repositories.StoryRepository

object Injection {
    private var application: Application? = null

    private inline val requireApplication
        get() = application ?: error("Missing: Injection Application")

    fun init(application: Application) {
        this.application = application
    }

    private val Context.dataStore by preferencesDataStore(name = "login")

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = AppPreferences.getInstance(requireApplication.dataStore)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(ApiConfig(pref).getApiService(), database)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = AppPreferences.getInstance(requireApplication.dataStore)
        return AuthRepository(ApiConfig(pref).getApiService(), pref)
    }


}