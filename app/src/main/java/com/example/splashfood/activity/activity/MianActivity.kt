package com.example.splashfood.activity.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.splashfood.R

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val isLogggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        Handler().postDelayed({
            if (!isLogggedIn) {
                val intent = Intent(this@MainActivity, logInActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this@MainActivity, WelcomeSplashFoodActivity::class.java)
                startActivity(intent)
            }
        }, 1000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
