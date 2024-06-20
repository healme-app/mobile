package com.capstone.healme

import android.os.Bundle
import androidx.activity.viewModels
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
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels {
            factory
        }

        mainViewModel = viewModel

        val navView: BottomNavigationView = binding.navView
        val bottomAppBar = binding.bottomAppBar
        val fab = binding.fab
        navView.background = null
        navView.menu.findItem(R.id.placeholder).isEnabled = false

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.HomeFragment, R.id.ProfileFragment
            )
        )

        mainViewModel.checkFirstOpen().observe(this) { firstOpen ->
            val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            if (firstOpen) {
                navGraph.setStartDestination(R.id.onboardingFragment)
                navController.graph = navGraph
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
            } else {
                mainViewModel.checkUserState().observe(this) { isLogin ->
                    if (isLogin) {
                        navGraph.setStartDestination(R.id.HomeFragment)
                    } else {
                        navGraph.setStartDestination(R.id.loginFragment)
                    }
                    navController.graph = navGraph
                    setupActionBarWithNavController(navController, appBarConfiguration)
                    navView.setupWithNavController(navController)
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment, R.id.placeholder, R.id.onboardingFragment -> {
                    bottomAppBar.gone()
                    fab.gone()
                    supportActionBar?.hide()
                }

                R.id.scanFragment, R.id.editProfileFragment, R.id.HealthcareFragment, R.id.HistoryFragment, R.id.resultFragment -> {
                    bottomAppBar.gone()
                    fab.gone()
                    supportActionBar?.show()
                }

                R.id.ProfileFragment, R.id.HomeFragment -> {
                    bottomAppBar.visible()
                    fab.visible()
                    supportActionBar?.hide()
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