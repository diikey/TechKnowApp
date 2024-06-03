package com.example.techknowapp.feature.course.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techknowapp.core.model.Announcement
import com.example.techknowapp.databinding.ListNewsBinding

class NewsRvAdapter(private val onListUpdate: () -> Unit, private val onItemClick : (Announcement) -> Unit) : RecyclerView.Adapter<NewsRvAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ListNewsBinding, private val onItemClick : (Announcement) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Announcement) {
            binding.tvTitle.text = currentItem.title
            binding.tvDescription.text = currentItem.description

            binding.linNews.setOnClickListener {
                onItemClick(currentItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ListNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding,onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[holder.adapterPosition]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Announcement>() {
        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)

    fun updateItems(newItems: List<Announcement>) {
        differ.submitList(newItems) {
            onListUpdate()
        }
    }
}