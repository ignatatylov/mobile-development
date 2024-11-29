package com.example.lab4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.databinding.ItemBuildingBinding
import android.content.Context
import android.widget.Toast


data class Building(
    val id: Int,
    val name: String,
    val imageResId: Int,
    val cost: Long,
    val canBuy: Boolean,
    val count: Int,
    val cookiesPerSecond: Double
)

class BuildingAdapter(
    private val onClick: (Building) -> Unit
) : ListAdapter<Building, BuildingAdapter.BuildingViewHolder>(BuildingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val binding = ItemBuildingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuildingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val building = getItem(position)
        holder.bind(building, holder.itemView.context)
    }

    inner class BuildingViewHolder(private val binding: ItemBuildingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(building: Building, context: Context) {
            binding.nameTextView.text = building.name
            binding.countTextView.text = "${building.count}"
            binding.costTextView.text = "${building.cost}"

            binding.imageView.setImageResource(building.imageResId)

            binding.nameTextView.alpha = if (building.canBuy) 1f else 0.5f
            binding.costTextView.alpha = if (building.canBuy) 1f else 0.5f
            binding.imageView.alpha = if (building.canBuy) 1f else 0.5f
            binding.countTextView.alpha = if (building.canBuy) 1f else 0.5f
            binding.imageIcon.alpha = if (building.canBuy) 1f else 0.5f

            binding.root.setOnClickListener {
                if (building.canBuy) {
                    onClick(building)
                } else {
                    Toast.makeText(
                        context,
                        "У вас недостаточно печенья для покупки \"${building.name}\"",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    class BuildingDiffCallback : DiffUtil.ItemCallback<Building>() {
        override fun areItemsTheSame(oldItem: Building, newItem: Building): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Building, newItem: Building): Boolean {
            return oldItem == newItem
        }
    }
}

