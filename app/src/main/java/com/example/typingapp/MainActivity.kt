package com.example.typingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val navMenu = findViewById<BottomNavigationView>(R.id.bottomNav)
        val appBarConfig = AppBarConfiguration(setOf(R.id.mainFragment, R.id.profileFragment, R.id.settingsFragment))
        setupActionBarWithNavController(navController, appBarConfig)

        navMenu.setupWithNavController(navController)
        initScores()
    }

    private fun initScores() {
        val wpms = arrayListOf<Int>()
        for (i in 1..10) {
            wpms.add(Random.nextInt(40, 100))
        }
        getSharedPreferences("Settings", 0).edit {
            putString("Scores", wpms.joinToString(","))
            apply()
        }
    }
}
