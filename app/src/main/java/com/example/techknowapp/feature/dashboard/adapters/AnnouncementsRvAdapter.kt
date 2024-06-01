package com.example.techknowapp.feature.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techknowapp.core.model.GlobalAnnouncement
import com.example.techknowapp.databinding.ListDashboardAnnouncementsBinding

class AnnouncementsRvAdapter(
    private val onListUpdate: () -> Unit,
    private val goToAnnouncementDetails: (GlobalAnnouncement) -> Unit
) : RecyclerView.Adapter<AnnouncementsRvAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListDashboardAnnouncementsBinding,
        private val goToAnnouncementDetails: (GlobalAnnouncement) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GlobalAnnouncement) {
            binding.tvAnnouncementTitle.text = data.title
            binding.tvAnnouncementDescription.text = data.description

            binding.linAnnouncement.setOnClickListener {
                goToAnnouncementDetails(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListDashboardAnnouncementsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding, goToAnnouncementDetails)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[holder.adapterPosition]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<GlobalAnnouncement>() {
        override fun areContentsTheSame(
            oldItem: GlobalAnnouncement,
            newItem: GlobalAnnouncement
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: GlobalAnnouncement,
            newItem: GlobalAnnouncement
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)

    fun updateItems(newItems: List<GlobalAnnouncement>) {
        differ.submitList(newItems) {
            onListUpdate()
        }
    }

    fun getItems(): List<GlobalAnnouncement> {
        return differ.currentList
    }
}