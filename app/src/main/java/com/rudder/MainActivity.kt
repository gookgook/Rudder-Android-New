package com.rudder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rudder.databinding.MainActivityBinding
import com.rudder.ui.main.MainFragment

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


    private fun setupJetpackNavigation() {
        val host =
            supportFragmentManager.findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment

        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)




        setupActionBarWithNavController(navController)


    }

}

