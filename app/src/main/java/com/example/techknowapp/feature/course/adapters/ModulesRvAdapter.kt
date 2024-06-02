package com.example.techknowapp.feature.course.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techknowapp.R
import com.example.techknowapp.core.model.CourseModule
import com.example.techknowapp.databinding.ListCourseModulesBinding

class ModulesRvAdapter(
    private val onListUpdate: () -> Unit,
    private val downloadModule: (CourseModule) -> Unit
) : RecyclerView.Adapter<ModulesRvAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListCourseModulesBinding,
        private val downloadModule: (CourseModule) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CourseModule) {
            binding.tvModuleName.text = data.name
            if (data.is_active) {
                binding.ivModuleIsLock.setImageResource(R.drawable.baseline_lock_open_24)
            } else {
                binding.ivModuleIsLock.setImageResource(R.drawable.baseline_lock_24)
            }

            binding.linModule.setOnClickListener {
                downloadModule(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ListCourseModulesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding, downloadModule)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[holder.adapterPosition]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<CourseModule>() {
        override fun areContentsTheSame(oldItem: CourseModule, newItem: CourseModule): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: CourseModule, newItem: CourseModule): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)

    fun updateItems(newItems: List<CourseModule>) {
        differ.submitList(newItems) {
            onListUpdate()
        }
    }

    fun getItems(): List<CourseModule> {
        return differ.currentList
    }
}