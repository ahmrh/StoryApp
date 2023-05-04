package com.ahmrh.storyapp.ui.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.data.local.Story
import com.ahmrh.storyapp.databinding.ItemRowStoryBinding
import com.bumptech.glide.Glide

class ListStoryAdapter(private val listStory: List<Story>): RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Story)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.tvItemTitle.text = listStory[position].name
        holder.binding.tvItemDesc.text = listStory[position].description
        holder.binding.tvItemCreatedAt.text = listStory[position].createdAt

        Glide.with(holder.itemView.context)
            .load(listStory[position].photoUrl)
            .into(holder.binding.imgItemPhoto)

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])
        }

    }
}