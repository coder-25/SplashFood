package com.example.splashfood.activity.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.splashfood.R
import com.example.splashfood.activity.fragment.*
import com.google.android.material.navigation.NavigationView

class WelcomeSplashFoodActivity : AppCompatActivity() {


    lateinit var drawer: DrawerLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var navigation: NavigationView
    lateinit var coordinate: CoordinatorLayout
    lateinit var frame: FrameLayout
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_welcome)

        setUpId()
        setUpToolbar()

        var previousItem: MenuItem? = null

        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frameLayout,
                AllRestaurentsFragment()
            ).commit()
        navigation.setCheckedItem(R.id.allRestaurant)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@WelcomeSplashFoodActivity,
            drawer,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigation.setNavigationItemSelectedListener {

            if(previousItem != null)
                previousItem?.isChecked=false

            it.isCheckable= true
            it.isChecked= true
            previousItem=it

            when(it.itemId){
                R.id.allRestaurant ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            AllRestaurentsFragment()
                        ).commit()
                    supportActionBar?.title="All restaurants"

                    drawer.closeDrawers()
                }

                R.id.favourite ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouriteFragment()
                        ).commit()
                    supportActionBar?.title="Favourite"

                    drawer.closeDrawers()
                }

                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        ).commit()
                    supportActionBar?.title="Profile"
                    drawer.closeDrawers()
                }

                R.id.history->{supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        OrderHistoryFragment()
                    ).commit()
                    supportActionBar?.title="Order History"
                    drawer.closeDrawers()

                }

                R.id.faq ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FaqFragment()
                        ).commit()
                    supportActionBar?.title="FAQ"
                    drawer.closeDrawers()

                }

                R.id.logOut-> {

                    val dailog=AlertDialog.Builder(this@WelcomeSplashFoodActivity)
                    dailog.setTitle("LOG OUT")
                    dailog.setMessage("Are you sure you want to log out")
                    dailog.setPositiveButton("Log Out"){ text,listner->
                        sharedPreferences.edit().clear().apply()
                        sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
                        val intent=Intent(this@WelcomeSplashFoodActivity,logInActivity::class.java)
                         startActivity(intent)
                    }
                    dailog.setNegativeButton("") { text, listner ->
                    }
                    dailog.create().show()


                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    private fun setUpId() {
        drawer = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbarLayout)
        navigation = findViewById(R.id.navigationLayout)
        coordinate = findViewById(R.id.coordinate)
        frame = findViewById(R.id.frameLayout)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val flag=supportFragmentManager.findFragmentById(R.id.frameLayout)

        when(flag){
            !is AllRestaurentsFragment ->{
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                       AllRestaurentsFragment()
                    )
                    .commit()
                supportActionBar?.title="All Restaurants"

            }
            else-> {
                finish()
            }
        }
    }

}
