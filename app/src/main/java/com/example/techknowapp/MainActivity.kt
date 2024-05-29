package com.example.techknowapp

import android.content.pm.PackageInfo
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.techknowapp.core.model.User
import com.example.techknowapp.core.utils.Cache
import com.example.techknowapp.databinding.ActivityMainBinding
import com.example.techknowapp.feature.dashboard.DashboardFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var cache: Cache

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
        cache = Cache(applicationContext)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.DashboardFragment) {
                supportActionBar!!.title = "Tech Know"
            }
        }
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        initComponents()
    }

    private fun initComponents() {
        val headerLayout = binding.navView.getHeaderView(0)
        val tvName = headerLayout.findViewById<TextView>(R.id.tv_name)
        val tvEmail = headerLayout.findViewById<TextView>(R.id.tv_email)
        val profile = headerLayout.findViewById<LinearLayout>(R.id.lin_profile)

        val type = object : TypeToken<User>() {}.type
        val userInfo = Gson().fromJson<User>(cache.getString(Cache.USER_INFO, ""), type)

        val packageManager = this.packageManager
        val packageInfo: PackageInfo
        var version = ""
        try {
            packageInfo = packageManager.getPackageInfo(this.packageName, 0)
            version = packageInfo.versionName
        } catch (e: Exception) {
            Timber.d("COULDN'T GET VERSION INFORMATION")
            e.printStackTrace()
        }

        /**
         * TEXTVIEWS
         */
        tvName.text = userInfo.username
        tvEmail.text = userInfo.email
        binding.tvAppVersion.text = getString(R.string.version_name, version)

        /**
         * ON CLICKS COMPONENTS
         */
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        binding.navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            AlertDialog.Builder(this).apply {
                title = "Logout"
                setMessage("Are you sure you want to logout?")
                setPositiveButton("YES") { dialog, _ ->
                    cache.save(Cache.TOKEN, "")
                    cache.save(Cache.USER_INFO, "")
                    dialog.dismiss()
                    finish()
                }
                setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
            }.show()
            true
        }

        profile.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.action_DashboardFragment_to_ProfileFragment)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}