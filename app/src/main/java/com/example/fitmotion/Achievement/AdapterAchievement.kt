package com.example.fitmotion.Achievement

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmotion.R

class AdapterAchievement(
    private val achievements: List<ModelAchievement>,
    private val achievementClickListener: AchievementClickListener
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
                    achievementClickListener.onAchievementClicked(achievement)
                }
            }

            cardView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val achievement = achievements[position]
                    achievementClickListener.onAchievementClicked(achievement)
                }
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.cream))

                itemView.postDelayed({
                    cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                }, 200)
            }
        }

        fun bind(achievement: ModelAchievement) {
            val drawableResId = itemView.context.resources.getIdentifier(
                achievement.imageName, "drawable", itemView.context.packageName
            )
            achievementImage.setImageResource(drawableResId)

            if (!achievement.isAchieved) {
                val grayColor = ContextCompat.getColor(itemView.context, R.color.gray)
                achievementImage.setColorFilter(grayColor)
            } else {
                achievementImage.colorFilter = null // Remove any previously applied color filter
            }

            achievementTitle.text = achievement.title
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
