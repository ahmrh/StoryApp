package com.ahmrh.storyapp.ui.story

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.ahmrh.storyapp.data.local.Story
import com.ahmrh.storyapp.databinding.ItemRowStoryBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

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
        holder.binding.tvItemCreatedAt.text = convertIsoToSimple(listStory[position].createdAt)

        Glide.with(holder.itemView.context)
            .load(listStory[position].photoUrl)
            .into(holder.binding.imgItemPhoto)

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])
        }

    }

    private fun convertIsoToSimple(isoDate: String): String {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(isoDate)

        val outputFormat = SimpleDateFormat("MMM dd, hh:mm", Locale.getDefault())

        return outputFormat.format(date as Date)
    }
}