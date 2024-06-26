package com.example.fitmotion.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fitmotion.Achievement.AchievementClickListener
import com.example.fitmotion.Achievement.AchievementFragment
import com.example.fitmotion.Achievement.AdapterAchievement
import com.example.fitmotion.Achievement.DetailsDialogFragment
import com.example.fitmotion.Achievement.ModelAchievement
import com.example.fitmotion.Factory.ViewModelFactory
import com.example.fitmotion.R
import com.example.fitmotion.Welcome.WelcomeActivity
import com.example.fitmotion.databinding.ActivityMainBinding
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity(), AchievementClickListener {

    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    companion object {
        val HOME_ITEM = R.id.homeFragment
        val ACHIEVEMENT_ITEM = R.id.achievementFragment
        val SENSOR_ITEM = R.id.sensorFragment
        val FRIENDS_ITEM = R.id.friendsFragment
        val PROFILE_ITEM = R.id.profileFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this){user ->
            if (!user.isLogin){
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }

        }



        initNavHost()
        binding.setUpBottomNavigation()
        setupView()

    }


    private fun initNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun ActivityMainBinding.setUpBottomNavigation() {
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, getString(R.string.home_nav), R.drawable.ic_home),
            CurvedBottomNavigation.Model(ACHIEVEMENT_ITEM, getString(R.string.achieve_nav), R.drawable.ic_achievement),
            CurvedBottomNavigation.Model(SENSOR_ITEM, getString(R.string.sensor_nav), R.drawable.ic_sensor),
            CurvedBottomNavigation.Model(FRIENDS_ITEM, getString(R.string.friends_nav), R.drawable.ic_friend),
            CurvedBottomNavigation.Model(PROFILE_ITEM, getString(R.string.profile_nav), R.drawable.ic_profile)
        )
        bottomNavigation.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                navController.navigate(it.id)
            }
            show(HOME_ITEM)
            setupNavController(navController)
        }
    }


    override fun onBackPressed() {
        if (navController.currentDestination!!.id == HOME_ITEM)
            super.onBackPressed()
        else {
            when (navController.currentDestination!!.id) {
                ACHIEVEMENT_ITEM, SENSOR_ITEM, FRIENDS_ITEM, PROFILE_ITEM -> {
                    navController.popBackStack(R.id.homeFragment, false)
                }
                else -> {
                    navController.navigateUp()
                }
            }
        }
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onAchievementClicked(achievement: ModelAchievement) {
        showDetailDialog(achievement)
    }

    private fun showDetailDialog(achievement: ModelAchievement) {
        val dialog = DetailsDialogFragment.newInstance(achievement.title, getString(achievement.descriptionResId))
        dialog.show(supportFragmentManager, "DetailDialogFragment")
    }
}
