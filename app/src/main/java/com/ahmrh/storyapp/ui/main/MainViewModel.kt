package com.ahmrh.storyapp.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmrh.storyapp.data.local.database.Story
import com.ahmrh.storyapp.data.repositories.StoryRepository
import java.io.File

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    companion object {
        const val TAG = "MainViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _pagingStory = getPagingStoryLiveData()
    val pagingStory: LiveData<PagingData<Story>> = _pagingStory

    private fun getPagingStoryLiveData(): LiveData<PagingData<Story>> {
        _isLoading.value = true
        val pagingStory = storyRepository.getStory().cachedIn(viewModelScope).asLiveData()
        pagingStory.observeForever(object : Observer<PagingData<Story>> {
            override fun onChanged(value: PagingData<Story>) {
                value.let {
                    _isLoading.value = false
                    pagingStory.removeObserver(this)
                }
            }
        })

        return pagingStory
    }

    fun uploadStory(file: File, description: String): LiveData<Boolean> {
        _isLoading.value = true
        val response = storyRepository.uploadStory(file, description)
        response.observeForever(object: Observer<Boolean>{
            override fun onChanged(value: Boolean) {
                value.let {
                    _isLoading.value = false
                    response.removeObserver(this)
                }
            }
        })

        return response
    }

}