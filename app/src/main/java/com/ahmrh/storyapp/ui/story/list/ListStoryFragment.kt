package com.ahmrh.storyapp.ui.story.list

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.data.local.database.Story
import com.ahmrh.storyapp.databinding.FragmentListStoryBinding
import com.ahmrh.storyapp.ui.main.MainActivity
import com.ahmrh.storyapp.ui.main.MainViewModel
import com.ahmrh.storyapp.ui.story.add.AddStoryFragment
import com.ahmrh.storyapp.ui.story.detail.DetailStoryFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
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
        mainViewModel.pagingStory.observe(requireActivity()){
            setStoriesData(it)
        }
        mainViewModel.isLoading.observe(requireActivity()){
            showLoading(it)
        }
    }

    private fun setStoriesData(pagingStory : PagingData<Story>){
        binding.rvStories.layoutManager = LinearLayoutManager(requireActivity())
        listStoryAdapter = ListStoryAdapter()

        binding.rvStories.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoryAdapter.retry()
            }
        )
        lifecycleScope.launch {
            listStoryAdapter.submitData(pagingStory)
        }

        listStoryAdapter.setOnItemClickCallback(object: ListStoryAdapter.OnItemClickCallback {
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