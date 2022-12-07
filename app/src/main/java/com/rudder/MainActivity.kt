package com.rudder

import KeyboardVisibilityUtils
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rudder.databinding.MainActivityBinding
import com.rudder.util.LoadingDialog
import com.rudder.util.setupWithNavController

class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }


    lateinit var fragmentCreated:(requestCode: Int, resultCode: Int, data: Intent?) -> Unit

    val dialog by lazy {
        LoadingDialog(this)
    }



    var isKeyboardUp: Boolean = false

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNavigationBar()

        val keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = {keyboardHeight, visibleDisplayFrameHeight ->
                // 키보드가 올라올 때의 동작
                Log.d("keyboard up", "up")
                isKeyboardUp = true
            },
            onHideKeyboard = {
                // 키보드가 내려갈 때의 동작
                Log.d("keyboard down", "down")
                isKeyboardUp = false
            }
        )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragmentCreated(requestCode,resultCode,data)
    }






    private lateinit var currentNavController: LiveData<NavController>

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navGraphIds = listOf(
            R.navigation.booksearch_nav_graph,
            R.navigation.host_nav_graph,
            R.navigation.application_nav_graph
        )
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

        controller.observe(this, Observer {
            it?.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    R.id.partyDetailFragment, R.id.createPartyFragment, R.id.applicantProfileFragment, R.id.chatFragment,
                    R.id.fragment_start,R.id.fragment_login,R.id.fragment_signup1,R.id.fragment_signup2,R.id.fragment_signup3,
                    R.id.partySettingFragment,R.id.myProfileFragment, R.id.partyEnquiryFragment, R.id.termsFragment
                    -> binding.bottomNavigationView.visibility = View.GONE
                    else -> binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    fun hideKeyboard() {
        if (this != null && this.currentFocus != null) {
            val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}

