package com.rudder

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowInsets
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rudder.databinding.MainActivityBinding
import com.rudder.util.setupWithNavController

class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNavigationBar()
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

  /*  private fun setupJetpackNavigation() {
        val host =
            supportFragmentManager.findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment

        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.partyDetailFragment,R.id.createPartyFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }*/

    private lateinit var currentNavController: LiveData<NavController>

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navGraphIds = listOf(R.navigation.booksearch_nav_graph, R.navigation.host_nav_graph,R.navigation.application_nav_graph)
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.booksearch_nav_host_fragment,
            intent = intent
        )

        /*controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })*/
        currentNavController = controller
        controller.value?.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.partyDetailFragment,R.id.createPartyFragment,R.id.applicantProfileFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}

