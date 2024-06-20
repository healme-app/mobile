package com.capstone.healme.ui.healthcare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.healme.data.remote.response.DataItem
import com.capstone.healme.databinding.ItemRowHealthcareBinding

class HealthcareAdapter(val updateCamera: (DataItem) -> Unit): ListAdapter<DataItem, HealthcareAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private var binding: ItemRowHealthcareBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(places: DataItem) {
            binding.apply {
                tvName.text = places.displayName
                tvLocation.text = places.shortFormattedAddress
                tvStatus.text = if (places.regularOpeningHours?.openNow == true) {
                    "Buka"
                } else {
                    "Tutup"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowHealthcareBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val places = getItem(position)
        holder.bind(places)

        holder.itemView.setOnClickListener {
            updateCamera(places)
        }
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}