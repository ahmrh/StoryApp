package com.ahmrh.storyapp

import com.ahmrh.storyapp.data.local.database.Story
import kotlin.text.Typography.quote

object DataDummy {

    fun generateDummyQuoteResponse(): List<Story> {
        val listStory: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "name $i",
                "description $i",
                "photoUrl $i",
                "createdAt + $i",
                0.0,
                0.0,
            )
            listStory.add(story)
        }
        return listStory
    }
}