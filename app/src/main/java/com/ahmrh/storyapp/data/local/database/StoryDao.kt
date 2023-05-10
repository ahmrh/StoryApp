package com.ahmrh.storyapp.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<Story>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteAllStories()

}