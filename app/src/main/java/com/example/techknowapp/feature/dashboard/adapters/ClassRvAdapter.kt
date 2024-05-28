package com.example.techknowapp.feature.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techknowapp.databinding.ListDashboardClassBinding

class ClassRvAdapter : RecyclerView.Adapter<ClassRvAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ListDashboardClassBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.tvClassName.text = data
            binding.tvClassDescription.text = "$data class description"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ListDashboardClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[holder.adapterPosition]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)

    fun updateItems(newItems: List<String>) {
        differ.submitList(newItems)
    }

    fun getItems(): List<String> {
        return differ.currentList
    }
}