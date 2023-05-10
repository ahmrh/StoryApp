package com.ahmrh.storyapp.ui.main

import android.location.Location
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmrh.storyapp.data.local.database.Story
import com.ahmrh.storyapp.data.repositories.StoryRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    companion object {
        const val TAG = "MainViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _pagingStory = MutableStateFlow(StoryState())
    val pagingStory = _pagingStory.asStateFlow()

    private val _mapStateFlow = MutableStateFlow(MapState())
    val mapStateFlow = _mapStateFlow.asStateFlow()

    fun fetchStories(){
        viewModelScope.launch {
            storyRepository.getStory().cachedIn(viewModelScope).collect{stories ->
                _pagingStory.update{
                    it.copy(pagingStory = stories)
                }
            }
        }
        viewModelScope.launch {
            storyRepository.getAllStoriesWithLocation().collect{stories ->
                _mapStateFlow.update {
                    it.copy(listStory = stories)
                }
            }
        }
    }


     fun uploadStory(file: File, description: String, location: Location?): LiveData<Boolean> {
        _isLoading.value = true
        val response = storyRepository.uploadStory(file, description, location)
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

    data class StoryState(
        val pagingStory: PagingData<Story> = PagingData.empty()
    )

    data class MapState(
        val listStory: List<Story> = emptyList()
    )

}