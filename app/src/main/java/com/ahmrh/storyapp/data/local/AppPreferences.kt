package com.ahmrh.storyapp.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val TAG = "AppPreferences"
        @Volatile
        private var INSTANCE: AppPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AppPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AppPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val NAME_KEY = stringPreferencesKey("name")
    private val LOGIN_KEY = booleanPreferencesKey("isLogin")

    suspend fun saveLogin(login: Login){
        dataStore.edit {preferences ->
            preferences[TOKEN_KEY] = "Bearer ${login.token}"
            preferences[NAME_KEY] = login.name
            preferences[LOGIN_KEY] = true
            Log.d(TAG, "SaveLogin: ${preferences[TOKEN_KEY]}")
        }
    }

    suspend fun deleteLogin(){
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[NAME_KEY] = ""
            preferences[LOGIN_KEY] = false
        }
    }

    fun isLogin() : Flow<Boolean>{
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    fun getToken(): Flow<String>{
        return dataStore.data.map { preferences->
            preferences[TOKEN_KEY] ?: "No Token"
        }
    }

    fun getName() : Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY] ?: ""
        }
    }
}