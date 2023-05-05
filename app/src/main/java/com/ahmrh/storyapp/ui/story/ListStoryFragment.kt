package com.ahmrh.storyapp.ui.story

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.data.local.Story
import com.ahmrh.storyapp.databinding.FragmentListStoryBinding
import com.ahmrh.storyapp.ui.main.MainActivity
import com.ahmrh.storyapp.ui.main.MainViewModel
import kotlinx.coroutines.launch

class ListStoryFragment : Fragment() {
    companion object{
        const val TAG = "ListStoryFragment"
    }
    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }

    private lateinit var listStoryAdapter: ListStoryAdapter

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mDetailStoryFragment: DetailStoryFragment
    private lateinit var mAddStoryFragment: AddStoryFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setupUtil()
        setupData()
        setupAction()
    }

    private fun setupAction() {
        binding.btnAddStory.setOnClickListener{
            mAddStoryFragment = AddStoryFragment()

            mFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.frame_container, mAddStoryFragment, DetailStoryFragment::class.java.simpleName)
            }
        }
    }

    private fun setupUtil() {
        mFragmentManager = parentFragmentManager
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
        listStoryAdapter = ListStoryAdapter(listStory)
        binding.rvStories.adapter = listStoryAdapter

        listStoryAdapter.setOnItemClickCallback(object: ListStoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Story){
                showSelectedStory(data)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSelectedStory(story: Story){

        mDetailStoryFragment = DetailStoryFragment()
        mDetailStoryFragment.story = story

        mFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.frame_container, mDetailStoryFragment, DetailStoryFragment::class.java.simpleName)
        }
    }
}