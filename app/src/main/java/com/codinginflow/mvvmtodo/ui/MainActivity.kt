package com.codinginflow.mvvmtodo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.codinginflow.mvvmtodo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.findNavController()

        // JetPack component will handle the back btn things
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        // When we press the back btn at the Add task Fragment.
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}