package com.example.splashfood.activity.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.splashfood.R
import com.example.splashfood.activity.adapter.cartAdapter
import com.example.splashfood.activity.database_menu.MenuEntities
import com.example.splashfood.activity.database_menu.menu_database
import com.example.splashfood.activity.model.FoodItem
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    lateinit var recyclerCart : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var adapterCart : cartAdapter
    lateinit var bttnProceedToPay: Button
    lateinit var txtResName:TextView
    lateinit var bttnok:Button
    lateinit var okPage:RelativeLayout
    lateinit var sharedPreferences: SharedPreferences
    var menuListDb= listOf<MenuEntities>()
    val menuList= arrayListOf<FoodItem>()
     var payment:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)
        title="My Cart"
        setUpId()
        sharedPreferences= getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)
        menuListDb=RetrieveData(this@CartActivity, 1).execute().get()
        for(i in 0 until menuListDb.size){
            val menuObjDb=menuListDb[i]
            val menuObj=FoodItem(
                menuObjDb.menuId.toString(),
                menuObjDb.menuName,
                menuObjDb.menuRate
            )
            menuList.add(menuObj)
        }

        txtResName.text=intent.getStringExtra("res_name")


        for(i in 0 until menuList.size){
            val menu= menuList[i]
            payment += menu.menuRate.toInt()
        }
        val bttnText:String? ="Place Order (Rs.$payment)"
        bttnProceedToPay.text=bttnText

        adapterCart= cartAdapter(this@CartActivity, menuList)
        layoutManager=LinearLayoutManager(this@CartActivity)
        recyclerCart.adapter=adapterCart
        recyclerCart.layoutManager=layoutManager

        recyclerCart.addItemDecoration(
            DividerItemDecoration(
                recyclerCart.context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )

        val queue=Volley.newRequestQueue(this@CartActivity)
        val url="http://13.235.250.119/v2/place_order/fetch_result/"

        val food=JSONArray()
        for(i in 0 until menuList.size){
            val foodId=JSONObject()
            val menu= menuList[i]
            foodId.put("food_item_id", menu.menuId )
            food.put(foodId)
        }
        val userId=sharedPreferences.getString("user_id","100")
        val param=JSONObject()
        param.put("user_id",userId)
        param.put("restaurant_id", intent.getStringExtra("res_Id"))
        param.put("total_cost",payment.toString())
        param.put("food",food)

        bttnProceedToPay.setOnClickListener {
            try {
                val jsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.POST, url, param, Response.Listener {
                        val data = it.getJSONObject("data")
                        if (data.getBoolean("success")) {
                            bttnProceedToPay.visibility= View.GONE
                            okPage.visibility = View.VISIBLE
                            bttnok.setOnClickListener {
                                RetrieveData(this@CartActivity,2).execute()
                                val intent= Intent(this@CartActivity, WelcomeSplashFoodActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@CartActivity,
                            "volley error occurred",
                            Toast.LENGTH_SHORT
                        ).show()

                    }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "8f6ac558d98837"
                            return headers
                        }
                    }
                queue.add(jsonObjectRequest)
            }catch (e: Exception){
                Toast.makeText(
                    this@CartActivity,
                    "volley error occurred",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }
    private fun setUpId(){
        recyclerCart= findViewById(R.id.cartRecycler)
        bttnProceedToPay = findViewById(R.id.cartProceed)
        txtResName=findViewById(R.id.txtCartRes)
        bttnok=findViewById(R.id.ok)
        okPage=findViewById(R.id.ok_page)
    }

    class RetrieveData(val context: Context, val mode:Int):AsyncTask<Void, Void, List<MenuEntities> >(){
        override fun doInBackground(vararg params: Void?): List<MenuEntities>? {
            val db= Room.databaseBuilder(context, menu_database::class.java, "menu-db").build()
            when(mode){
                1 ->{
                    return db.daoMenu().getAllRestaurants()
                }
                2 ->{
                    db.clearAllTables()
                    db.close()
                }
            }
            return null
        }

    }

    override fun onBackPressed() {
        RetrieveData(this@CartActivity,2).execute()
        val intent= Intent(this@CartActivity, WelcomeSplashFoodActivity::class.java)
         startActivity(intent)
        finish()
        super.onBackPressed()
    }

}
