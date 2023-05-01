package com.ahmrh.storyapp.api.responses

import com.google.gson.annotations.SerializedName

data class ListStoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<StoryItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)