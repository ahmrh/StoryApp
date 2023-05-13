package com.ahmrh.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.ahmrh.storyapp.DataDummy
import com.ahmrh.storyapp.MainDispatcherRule
import com.ahmrh.storyapp.data.local.database.Story
import com.ahmrh.storyapp.data.remote.StoryPagingSource
import com.ahmrh.storyapp.data.repositories.StoryRepository
import com.ahmrh.storyapp.ui.story.list.ListStoryAdapter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)

        val storyRepositoryMock = mockk<StoryRepository>()
        coEvery{ storyRepositoryMock.getStory()} returns flowOf(data)
        coEvery { storyRepositoryMock.getAllStoriesWithLocation() } returns flowOf(dummyStory)

        val mainViewModel = MainViewModel(storyRepositoryMock)
        mainViewModel.fetchStories()

        val actualStory: PagingData<Story> = mainViewModel.pagingStory.value.pagingStory

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])

    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val dummyStory = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<Story> = PagingData.from(emptyList())

        val storyRepositoryMock = mockk<StoryRepository>()
        coEvery{ storyRepositoryMock.getStory()} returns flowOf(data)
        coEvery { storyRepositoryMock.getAllStoriesWithLocation() } returns flowOf(dummyStory)

        val mainViewModel = MainViewModel(storyRepositoryMock)
        mainViewModel.fetchStories()
        val actualStory: PagingData<Story> = mainViewModel.pagingStory.value.pagingStory

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }

    class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
        companion object {
            fun snapshot(items: List<Story>): PagingData<Story> {
                return PagingData.from(items)
            }
        }
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
            return 0
        }
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
}


val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
