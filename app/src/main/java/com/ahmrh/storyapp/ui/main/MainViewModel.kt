package com.ahmrh.storyapp.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.ahmrh.storyapp.data.local.AppPreferences
import com.ahmrh.storyapp.data.local.Story
import com.ahmrh.storyapp.data.remote.responses.ListStoryResponse
import com.ahmrh.storyapp.data.remote.responses.StoryItem
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import com.ahmrh.storyapp.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: AppPreferences) : ViewModel() {
    companion object{
        const val TAG = "MainViewModel"
    }


    private val _listStory = MutableLiveData<List<Story>>()
    val listStory: LiveData<List<Story>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

   fun getToken() : LiveData<String> = pref.getToken().asLiveData()

    fun fetchStories(token : String) {
        _isLoading.value = true
        Log.d(TAG, token)
        val client = ApiConfig.getApiService().getAllStories(token)
        client.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>,
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { buildListStory(it.listStory) }
                    _isLoading.value = false

                } else {
                    _isLoading.value = false
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailureThrowable: ${t.message}")
            }
        })
    }

    private fun buildListStory(listStoryItem: List<StoryItem>) {
        val listStory: List<Story> = listStoryItem.map { storyItem ->
            Story(
                storyItem.id,
                storyItem.name,
                storyItem.description,
                storyItem.photoUrl,
                storyItem.createdAt,
                (storyItem.lat ?: 0.0) as Double,
                (storyItem.lon ?: 0.0) as Double
            )
        }

        _listStory.value = listStory

    }

}