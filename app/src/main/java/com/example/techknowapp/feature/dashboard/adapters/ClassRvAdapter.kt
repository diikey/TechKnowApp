package com.example.techknowapp.feature.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.techknowapp.R
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.databinding.ListDashboardClassBinding

class ClassRvAdapter(
    private val onListUpdate: () -> Unit,
    private val goToCourseDetails: (Course) -> Unit
) : RecyclerView.Adapter<ClassRvAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListDashboardClassBinding,
        private val goToCourseDetails: (Course) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Course) {
            binding.tvClassName.text = data.name
            binding.tvClassDescription.text = data.description
            if (data.image != null) {
                binding.ivCourseImage.load(data.image)
                binding.ivCourseImage.visibility = View.VISIBLE
                binding.tvImagePlacheholder.visibility = View.GONE
            } else {
                binding.ivCourseImage.visibility = View.VISIBLE
                binding.tvImagePlacheholder.visibility = View.GONE
                binding.ivCourseImage.setImageResource(R.drawable.baseline_broken_image_24)
            }

            binding.linCourse.setOnClickListener {
                goToCourseDetails(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ListDashboardClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding, goToCourseDetails)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[holder.adapterPosition]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Course>() {
        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)

    fun updateItems(newItems: List<Course>) {
        differ.submitList(newItems) {
            onListUpdate()
        }
    }

    fun getItems(): List<Course> {
        return differ.currentList
    }
}