package com.capstone.healme.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.healme.R
import com.capstone.healme.data.local.entity.ScanEntity
import com.capstone.healme.databinding.FragmentEditProfileBinding
import com.capstone.healme.databinding.ItemRowHistoryBinding

class HistoryAdapter: ListAdapter<ScanEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("resultId", history.resultId)
            holder.itemView.findNavController()
                .navigate(R.id.action_HistoryFragment_to_resultFragment, bundle)
        }
    }

    class MyViewHolder(val binding: ItemRowHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(scanEntity: ScanEntity) {
            binding.apply {
                Glide.with(ivWound)
                    .load(scanEntity.imageUrl)
                    .into(ivWound)
                tvWoundType.text = scanEntity.result
                tvConfidenceScore.text = scanEntity.confidenceScore.toString()
                tvDateCreated.text = scanEntity.createdAt
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ScanEntity> =
            object : DiffUtil.ItemCallback<ScanEntity>() {
                override fun areItemsTheSame(oldItem: ScanEntity, newItem: ScanEntity): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: ScanEntity, newItem: ScanEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}