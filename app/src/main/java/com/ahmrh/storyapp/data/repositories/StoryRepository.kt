package com.ahmrh.storyapp.data.repositories

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.ahmrh.storyapp.data.local.database.Story
import com.ahmrh.storyapp.data.local.database.StoryDatabase
import com.ahmrh.storyapp.data.remote.StoryRemoteMediator
import com.ahmrh.storyapp.data.remote.responses.DefaultResponse
import com.ahmrh.storyapp.data.remote.responses.StoryItem
import com.ahmrh.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository(private val apiService: ApiService, private val storyDatabase: StoryDatabase){
    fun getStory(): Flow<PagingData<Story>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),
            remoteMediator = StoryRemoteMediator(apiService, storyDatabase),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).flow
    }

    suspend fun getAllStoriesWithLocation() : Flow<List<Story>> {
        val response = apiService.getAllStoriesWithLocation(1)
        val listStory: List<Story> = buildListStory(response.listStory)

        return flowOf(listStory)
    }

     fun uploadStory(file: File, description: String, location: Location?): LiveData<Boolean>{

        val uploadSuccessLiveData = MutableLiveData<Boolean>()

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

         val latitiude = location?.latitude
         val longitude = location?.longitude
        val client = apiService.addStory(imageMultipart, requestDescription, latitiude,longitude )

        client.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                uploadSuccessLiveData.value = response.isSuccessful
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                uploadSuccessLiveData.value = false
            }
        })


        return uploadSuccessLiveData
    }


    private fun buildListStory(listStoryItem: List<StoryItem>): List<Story> {
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

        return listStory
    }
    companion object{
        const val TAG = "StoryRepository"
    }

}