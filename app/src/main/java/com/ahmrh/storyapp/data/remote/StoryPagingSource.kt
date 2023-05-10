package com.ahmrh.storyapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmrh.storyapp.data.local.database.Story
import com.ahmrh.storyapp.data.remote.responses.ListStoryResponse
import com.ahmrh.storyapp.data.remote.responses.StoryItem
import com.ahmrh.storyapp.data.remote.retrofit.ApiConfig
import com.ahmrh.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {
    private companion object {
        const val TAG = "StoryPagingSource"
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllStories(position, params.loadSize)

            val listStory = buildListStory(response.listStory)


            LoadResult.Page(
                data = listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
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


}