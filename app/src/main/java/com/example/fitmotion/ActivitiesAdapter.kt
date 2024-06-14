package com.example.fitmotion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmotion.databinding.ItemActivitiesBinding


data class ActivitiesItem(val imageResId: Int, val name: String, val duration: String)
class ActivitiesAdapter(private val activities: List<ActivitiesItem>) : RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder>(){

    inner class ActivityViewHolder(val binding: ItemActivitiesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: ActivitiesItem, position: Int) {
            binding.activityImage.setImageResource(activity.imageResId)
            binding.activityName.text = activity.name
            binding.activityDuration.text = activity.duration
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun getItemCount(): Int = activities.size

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(activities[position], position)
    }
}