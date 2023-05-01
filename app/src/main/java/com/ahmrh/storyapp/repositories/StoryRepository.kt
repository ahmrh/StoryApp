package com.ahmrh.storyapp.repositories

import android.util.Log
import com.ahmrh.storyapp.data.remote.responses.DetailStoryResponse
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository {
    companion object{
        const val TAG = "StoryRepository"
    }

    private lateinit var token: String
    init{
        val token = ""
    }
    fun getStory(id: Int) {
        if(token != null) {
            val client = ApiConfig.getApiService().getDetailStory(id, token)
            client.enqueue(object : Callback<DetailStoryResponse> {
                override fun onResponse(
                    call: Call<DetailStoryResponse>,
                    response: Response<DetailStoryResponse>
                ) {
                    if (response.isSuccessful) {
                    } else {
                        Log.e(AuthRepository.TAG, "onFailureResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    Log.e(AuthRepository.TAG, "onFailureThrowable: ${t.message}")
                }
            })
        }
    }
}