package com.ahmrh.storyapp.api

import com.ahmrh.storyapp.api.responses.DefaultResponse
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
}