package com.ahmrh.storyapp.ui.story.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmrh.storyapp.data.local.database.Story
import com.ahmrh.storyapp.databinding.ItemRowStoryBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class ListStoryAdapter: PagingDataAdapter<Story, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK)  {
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


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position) ?: return
        holder.binding.tvItemTitle.text = story.name
        holder.binding.tvItemDesc.text = story.description
        holder.binding.tvItemCreatedAt.text = convertIsoToSimple(story.createdAt)

        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.binding.imgItemPhoto)

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(story)
        }

    }

    private fun convertIsoToSimple(isoDate: String): String {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(isoDate)

        val outputFormat = SimpleDateFormat("MMM dd, hh:mm", Locale.getDefault())

        return outputFormat.format(date as Date)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}