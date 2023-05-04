package com.ahmrh.storyapp.ui.story

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.data.local.Story
import com.ahmrh.storyapp.databinding.FragmentDetailStoryBinding
import com.ahmrh.storyapp.databinding.FragmentListStoryBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class DetailStoryFragment : Fragment() {
    var story: Story? = null
    companion object{
        const val TAG = "DetailStoryFragment"
    }

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, story.toString())

        setupData()
    }

    private fun setupData() {

        binding.tvDetailName.text = story?.name ?: "Unnamed Story"
        binding.tvDetailDesc.text = story?.description ?: "No Description Available"

        val placeholderUrl = "https://images.pexels.com/photos/776655/pexels-photo-776655.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        Glide.with(requireActivity())
            .load(story?.photoUrl ?: placeholderUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imgDetailPhoto)
    }

}