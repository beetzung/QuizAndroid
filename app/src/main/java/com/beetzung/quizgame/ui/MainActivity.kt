package com.beetzung.quizgame.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.beetzung.quizgame.R
import com.beetzung.quizgame.data.prefs.Preferences

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "QuizzApp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Preferences.init(this)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        with(findViewById<Toolbar>(R.id.toolbar)) {
            setupWithNavController(
                navController,
                AppBarConfiguration(navController.graph)
            )
            setSupportActionBar(this)
        }

    }
}