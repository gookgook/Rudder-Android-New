package com.rudder

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rudder.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupJetpackNavigation()
    }

    fun getDisplaySize(): ArrayList<Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = this@MainActivity.windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            val width = windowMetrics.bounds.width() - insets.left - insets.right
            val height = windowMetrics.bounds.height() - insets.top - insets.bottom
            arrayListOf(width, height)
        } else {
            val displayMertrics = DisplayMetrics()
            this.windowManager.defaultDisplay.getMetrics(displayMertrics)
            val width = displayMertrics.widthPixels
            val height = displayMertrics.heightPixels
            arrayListOf(width, height)
        }
    }

    private fun setupJetpackNavigation() {
        val host =
            supportFragmentManager.findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment

        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.partyDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }




    }

}

