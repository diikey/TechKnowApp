package com.example.techknowapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.techknowapp.databinding.ActivityMainBinding
import com.example.techknowapp.feature.dashboard.DashboardFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val onBackCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            val fragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
            val backStackCount = fragment.childFragmentManager.backStackEntryCount
            Timber.d("onBackPressed>>>count\t$backStackCount")

            if (fragment.childFragmentManager.fragments[0] is DashboardFragment) {
                Timber.d("onBackPressed>>>EXITAPP1")
                finishAffinity()
            } else {
                if (backStackCount == 0) {
                } else {
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackCallback)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        initComponents()
    }

    private fun initComponents() {
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}