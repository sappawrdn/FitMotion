package com.example.fitmotion.Achievement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmotion.R

class AdapterAchievement(
    private val achievements: List<ModelAchievement>,
    private val parentFragment: Fragment
) : RecyclerView.Adapter<AdapterAchievement.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val achievementImage: ImageView = itemView.findViewById(R.id.achievement_image)
        private val achievementTitle: TextView = itemView.findViewById(R.id.achievement_title)
        private val cardView: CardView = itemView.findViewById(R.id.cv_achievement)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val achievement = achievements[position]
                    showDetailDialog(achievement)
                }
            }

            cardView.setOnClickListener {
                cardView.setCardBackgroundColor(
                    itemView.context.getColor(R.color.cream)
                )
                itemView.postDelayed({
                    cardView.setCardBackgroundColor(
                        itemView.context.getColor(android.R.color.white)
                    )
                }, 200)
            }
        }

        fun bind(achievement: ModelAchievement) {
            val drawableResId = itemView.context.resources.getIdentifier(
                achievement.imageName, "drawable", itemView.context.packageName
            )
            achievementImage.setImageResource(drawableResId)

            if (!achievement.isAchieved) {
                achievementImage.setColorFilter(itemView.context.getColor(R.color.gray))
            } else {
                achievementImage.clearColorFilter()
            }

            achievementTitle.text = achievement.title
        }

        private fun showDetailDialog(achievement: ModelAchievement) {
            val description = itemView.context.getString(achievement.descriptionResId)
            val dialogFragment = DetailsDialogFragment.newInstance(achievement.title, description)
            dialogFragment.show(parentFragment.childFragmentManager, "DetailDialogFragment")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievements, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(achievements[position])
    }

    override fun getItemCount(): Int {
        return achievements.size
    }
}
