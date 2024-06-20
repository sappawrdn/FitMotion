package com.example.fitmotion.Achievement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmotion.R

class AchievementFragment : Fragment(), AchievementClickListener {

    private lateinit var achievementsRecyclerView: RecyclerView
    private lateinit var achievementAdapter: AdapterAchievement

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_achievement, container, false)

        // Initialize RecyclerView
        achievementsRecyclerView = view.findViewById(R.id.achievements_rv)
        achievementsRecyclerView.layoutManager = GridLayoutManager(context, 2)

        // Define list of achievements
        val achievements = listOf(
            ModelAchievement("Runner", true, "bdg_runner", R.string.runner),
            ModelAchievement("Marathon", true, "bdg_marathon", R.string.marathon),
            ModelAchievement("Achilles", true, "bdg_achilles", R.string.achilles),
            ModelAchievement("Pedestrian", true, "bdg_pedestrian", R.string.pedestrian),
            ModelAchievement("Adventurer", true, "bdg_adventurer", R.string.adventurer),
            ModelAchievement("Journeyman", false, "bdg_journeyman", R.string.journey_man),
            ModelAchievement("Triple", true, "bdg_triple", R.string.triple),
            ModelAchievement("Halfmoon", false, "bdg_halfmoon", R.string.halfmoon),
            ModelAchievement("To Infinity", false, "bdg_toinfinity", R.string.to_infinity),
            ModelAchievement("Nailed It", true, "bdg_nailedit", R.string.nailed_it),
            ModelAchievement("Go Beyond", false, "bdg_gobeyond", R.string.go_beyond),
            ModelAchievement("Fullmoon", false, "bdg_fullmoon", R.string.fullmoon),

        )

        // Initialize adapter with achievements list
        achievementAdapter = AdapterAchievement(achievements, this)
        // Set adapter to RecyclerView
        achievementsRecyclerView.adapter = achievementAdapter

        return view
    }

    override fun onAchievementClicked(achievement: ModelAchievement) {
        Log.d("AchievementFragment", "Achievement clicked: ${achievement.title}")

        // Atau, tampilkan dialog detail achievement
        val fragmentManager = requireActivity().supportFragmentManager
        val description = getString(achievement.descriptionResId)
        val dialogFragment = DetailDialogFragment.newInstance(achievement.title, description)
        dialogFragment.show(fragmentManager, "DetailDialogFragment")
    }


}
