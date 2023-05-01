package com.ahmrh.storyapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
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
            preferences[TOKEN_KEY] = login.token
            preferences[NAME_KEY] = login.name
            preferences[LOGIN_KEY] = true
        }
    }

    fun isLogin() : Flow<Boolean>{
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    fun getName() : Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY] ?: ""
        }
    }
}