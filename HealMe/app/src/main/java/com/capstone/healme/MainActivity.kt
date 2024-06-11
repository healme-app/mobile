package com.capstone.healme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.healme.databinding.ActivityMainBinding
import com.capstone.healme.extension.gone
import com.capstone.healme.extension.visible
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val bottomAppBar = binding.bottomAppBar
        val fab = binding.fab
        navView.background = null
        navView.menu.findItem(R.id.placeholder).isEnabled = false

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_settings
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    bottomAppBar.gone()
                    fab.gone()
                    supportActionBar?.hide()
                }

                R.id.scanFragment -> {
                    bottomAppBar.gone()
                    fab.gone()
                    supportActionBar?.show()
                }

                else -> {
                    bottomAppBar.visible()
                    fab.visible()
                    supportActionBar?.show()
                }
            }
        }

        fab.setOnClickListener {
            navController.navigate(R.id.scanFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}