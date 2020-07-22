package com.example.splashfood.activity.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.splashfood.R
import com.example.splashfood.activity.adapter.menuDescriptionAdapter
import com.example.splashfood.activity.database_menu.MenuEntities
import com.example.splashfood.activity.database_menu.menu_database
import com.example.splashfood.activity.model.ResMenuDescription
import com.example.splashfood.activity.util.CollectionManager

class MenuDescriptionActivity : AppCompatActivity() {

    var resId:String? ="100001"
    val menuList= arrayListOf<ResMenuDescription>()

    lateinit var menuRecycler:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var menuAdapter:menuDescriptionAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var bttnAddToCart:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_description)
        val ResName=intent.getStringExtra("Res_Name")
        title=ResName

        setUpId()

        if(resId != null){
            resId= intent.getStringExtra("Res_Id")
        }
        else{
            Toast.makeText(
                this@MenuDescriptionActivity,
                "An unexpected error occurred(intent id is null )",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        if(resId=="100001"){
            Toast.makeText(
                this@MenuDescriptionActivity,
                "An unexpected error occurred(intent did not work)",
                Toast.LENGTH_SHORT
            ).show()
           finish()

        }


        val queue= Volley.newRequestQueue(this@MenuDescriptionActivity)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/$resId"


        if(CollectionManager().checkConnectivity(this@MenuDescriptionActivity)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val dataObjectArray = data.getJSONArray("data")
                        for (i in 0 until dataObjectArray.length()) {
                            val dataObject = dataObjectArray.getJSONObject(i)
                            val menu = ResMenuDescription(
                                dataObject.getString("id"),
                                dataObject.getString("name"),
                                dataObject.getString("cost_for_one"),
                                dataObject.getString("restaurant_id")
                            )
                            menuList.add(menu)
                        }
                        progressLayout.visibility = View.GONE

                        layoutManager = LinearLayoutManager(this@MenuDescriptionActivity)
                        menuAdapter = menuDescriptionAdapter(
                            this@MenuDescriptionActivity,
                            menuList,
                            bttnAddToCart
                        )
                        menuRecycler.adapter = menuAdapter
                        menuRecycler.layoutManager = layoutManager

                        bttnAddToCart.setOnClickListener {
                            val nxtCart =
                                Intent(this@MenuDescriptionActivity, CartActivity::class.java)
                            nxtCart.putExtra("res_Id", menuList[0].mealResId)
                            nxtCart.putExtra("res_name", ResName)
                            startActivity(nxtCart)
                            finish()
                        }

                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@MenuDescriptionActivity,
                        "An unexpected error occurred(intent id is null )",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "8f6ac558d98837"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }
        else{
            val dailog = AlertDialog.Builder(this@MenuDescriptionActivity)
            dailog.setTitle("Error")
            dailog.setMessage("Error In Internet Connection Found")
            dailog.setPositiveButton("Open Settings"){text,listner->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)    /// expilicit intent to open phone functions out ogf the app
                startActivity(settingIntent)
                finish()
            }
            dailog.setNegativeButton("Exit") { text, listner ->
                finish()
            }
            dailog.create().show()

        }

    }

    private fun setUpId(){
        bttnAddToCart=findViewById(R.id.bttnAddCart)
        menuRecycler=findViewById(R.id.menuRecyclerLayout)
        progressLayout=findViewById(R.id.menuProgressLayout)
    }

    class asyncTask(val context: Context, val mode: Int): AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, menu_database::class.java, "menu-db").build()
            when(mode){

                1 ->{
                    val obj: List<MenuEntities>? = db.daoMenu().getAllRestaurants()
                    db.close()
                    return obj != null
                }
                2 ->{
                    db.clearAllTables()
                    db.close()
                    return true
                }
            }
           return false
        }
    }

    override fun onBackPressed() {
        asyncTask(this@MenuDescriptionActivity,2).execute()
        super.onBackPressed()
    }
}
