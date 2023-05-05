package com.ahmrh.storyapp.ui.main

import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.ahmrh.storyapp.data.local.AppPreferences
import com.ahmrh.storyapp.data.local.Story
import com.ahmrh.storyapp.data.remote.responses.DefaultResponse
import com.ahmrh.storyapp.data.remote.responses.ListStoryResponse
import com.ahmrh.storyapp.data.remote.responses.StoryItem
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import com.ahmrh.storyapp.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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

    fun uploadStory(file: File, description: String, token: String): Boolean {
        var uploadSuccess = false

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val uploadImageRequest = ApiConfig.getApiService().addStory(imageMultipart, requestDescription, token)

        uploadImageRequest.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    uploadSuccess = true
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                uploadSuccess = false
            }
        })
        return uploadSuccess
    }

}