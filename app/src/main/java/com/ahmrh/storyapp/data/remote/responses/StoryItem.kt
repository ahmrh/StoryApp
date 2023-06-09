package com.ahmrh.storyapp.data.remote.responses

import com.google.gson.annotations.SerializedName

data class StoryItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,


	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lon")
	val lon: Any,

	@field:SerializedName("lat")
	val lat: Any
)