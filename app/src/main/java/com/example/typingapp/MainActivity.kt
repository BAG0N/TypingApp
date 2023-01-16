package com.example.typingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

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
    }
}
