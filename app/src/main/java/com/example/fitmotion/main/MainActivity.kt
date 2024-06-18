package com.example.fitmotion.main

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fitmotion.Factory.ViewModelFactory
import com.example.fitmotion.R
import com.example.fitmotion.Sensor.SensorData
import com.example.fitmotion.Sensor.SensorRoomDatabase
import com.example.fitmotion.Welcome.WelcomeActivity
import com.example.fitmotion.databinding.ActivityMainBinding
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    // sappwrdn
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    //variabel sensor

    private lateinit var sensorManager: SensorManager
    private val attitudeReading = FloatArray(3)
    private val gravityReading = FloatArray(3)
    private val gyroscopeReading = FloatArray(3)
    private val accelerometerReading = FloatArray(3)

    private val handler = Handler(Looper.getMainLooper())
    private val saveInterval = 20L

    private val checkHandler = Handler(Looper.getMainLooper())
    private val checkInterval = 1000L // 1 detik

    // Counter untuk jumlah data yang disimpan
    private var dataCounter = 0

    //variabel room database untuk sensor
    private lateinit var sensordatabase: SensorRoomDatabase

    companion object {
        val HOME_ITEM = R.id.homeFragment
        val ACHIEVEMENT_ITEM = R.id.achievementFragment
        val SENSOR_ITEM = R.id.sensorFragment
        val FRIENDS_ITEM = R.id.friendsFragment
        val PROFILE_ITEM = R.id.profileFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensordatabase = SensorRoomDatabase.getDatabase(this)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        initNavHost()
        binding.setUpBottomNavigation()
        setupView()
        registerSensors()
        startSavingDataPeriodically()
        startCheckingData()
        checkSensorAvailability(Sensor.TYPE_GAME_ROTATION_VECTOR)
        checkSensorAvailability(Sensor.TYPE_GRAVITY)
        checkSensorAvailability(Sensor.TYPE_GYROSCOPE)
        checkSensorAvailability(Sensor.TYPE_LINEAR_ACCELERATION)

    }

    private fun startSavingDataPeriodically() {
        val saveDataRunnable = object : Runnable {
            override fun run() {
                saveSensorData()
                handler.postDelayed(this, saveInterval)
            }
        }
        handler.post(saveDataRunnable)
    }

    private fun saveSensorData() {
        val sensorData = SensorData().apply {
            attitudepitch = convertToPlainString(attitudeReading[0])
            attituderoll = convertToPlainString(attitudeReading[1])
            attitudeazimuth = convertToPlainString(attitudeReading[2])
            gravityx = convertToPlainString(gravityReading[0])
            gravityy = convertToPlainString(gravityReading[1])
            gravityz = convertToPlainString(gravityReading[2])
            rotationratex = convertToPlainString(gyroscopeReading[0])
            rotationratey = convertToPlainString(gyroscopeReading[1])
            rotationratez = convertToPlainString(gyroscopeReading[2])
            useraccelerationx = convertToPlainString(accelerometerReading[0])
            useraccelarationy = convertToPlainString(accelerometerReading[1])
            useraccelerationz = convertToPlainString(accelerometerReading[2])
        }

        Log.d("SensorData", "Prepared SensorData object: $sensorData")
        lifecycleScope.launch(Dispatchers.IO) {
            sensordatabase.sensorDao().insert(sensorData)
            dataCounter++
        }
    }

    private fun convertToPlainString(value: Float): String {
        return BigDecimal(value.toString()).toPlainString()
    }

    private fun registerSensors() {
        val sensors = listOf(
            Sensor.TYPE_GAME_ROTATION_VECTOR,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_LINEAR_ACCELERATION
        )

        sensors.forEach { sensorType ->
            sensorManager.getDefaultSensor(sensorType)?.also { sensor ->
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    private fun checkSensorAvailability(sensorType: Int) {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        if (sensor != null) {
            Log.d("SensorAvailability", "Sensor with type $sensorType is available on this device.")
        } else {
            Log.d("SensorAvailability", "Sensor with type $sensorType is not available on this device.")
        }
    }

    private fun startCheckingData() {
        val checkDataRunnable = object : Runnable {
            override fun run() {
                Log.d("DataCounter", "Number of rows inserted in the last second: $dataCounter")
                dataCounter = 0 // Reset counter
                checkHandler.postDelayed(this, checkInterval)
            }
        }
        checkHandler.post(checkDataRunnable)
    }

    override fun onResume() {
        super.onResume()
        registerSensors()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if(event == null){
            return
        }

        if (event.sensor.type == Sensor.TYPE_GAME_ROTATION_VECTOR){
            System.arraycopy(event.values, 0, attitudeReading, 0, attitudeReading.size)
        }else if (event.sensor.type == Sensor.TYPE_GRAVITY){
            System.arraycopy(event.values, 0, gravityReading, 0, gravityReading.size)
        }else if (event.sensor.type == Sensor.TYPE_GYROSCOPE){
            System.arraycopy(event.values, 0, gyroscopeReading, 0, gyroscopeReading.size)
        }else if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION){
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("sensor Accuracy", "Sensor Accuracy changed: $accuracy")
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
}
