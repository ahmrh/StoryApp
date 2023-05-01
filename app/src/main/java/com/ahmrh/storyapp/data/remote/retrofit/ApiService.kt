package com.ahmrh.storyapp.data.remote.retrofit

import com.ahmrh.storyapp.data.remote.responses.DefaultResponse
import com.ahmrh.storyapp.data.remote.responses.DetailStoryResponse
import com.ahmrh.storyapp.data.remote.responses.ListStoryResponse
import com.ahmrh.storyapp.data.remote.responses.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<ListStoryResponse>


    @GET("stories/{id}")
    fun getDetailStory(
        @Part("id") id : Int,
        @Header("Authorization") token: String
    ): Call<DetailStoryResponse>




}