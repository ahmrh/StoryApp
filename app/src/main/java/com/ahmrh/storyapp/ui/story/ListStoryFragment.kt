package com.ahmrh.storyapp.ui.story

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.data.local.Story
import com.ahmrh.storyapp.databinding.FragmentListStoryBinding
import com.ahmrh.storyapp.ui.main.MainActivity
import com.ahmrh.storyapp.ui.main.MainViewModel

class ListStoryFragment : Fragment() {
    companion object{
        const val TAG = "ListStoryFragment"
    }
    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val mainViewModel: MainViewModel by lazy {
         (activity as MainActivity).mainViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setupData()
    }


    private fun setupData() {
        mainViewModel.getToken().observe(requireActivity()){
            mainViewModel.fetchStories(it)
            Log.d(TAG, "Token: $it")
        }
        mainViewModel.listStory.observe(requireActivity()){ listStory ->
            setStoriesData(listStory)
        }
        mainViewModel.isLoading.observe(requireActivity()){
            showLoading(it)
        }
    }

    private fun setStoriesData(listStory: List<Story>){
        binding.rvStories.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = ListStoryAdapter(listStory)
        binding.rvStories.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}