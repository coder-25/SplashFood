package com.example.splashfood.activity.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.splashfood.R
import com.example.splashfood.activity.util.CollectionManager
import org.json.JSONObject

class logInActivity : AppCompatActivity() {


    lateinit var logIn: Button
    lateinit var phoneNo: EditText
    lateinit var password: EditText
    lateinit var signUp: TextView
    lateinit var forgot: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        logIn=findViewById(R.id.bttnLog)
        phoneNo=findViewById(R.id.txt_moble)
        password=findViewById(R.id.txt_password)
        signUp=findViewById(R.id.txt_sign)
        forgot=findViewById(R.id.txt_fogot)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        logIn.setOnClickListener {
            Unit
            val inputPhone=phoneNo.text.toString()
            val inputPassword=password.text.toString()

            val queue=Volley.newRequestQueue(this@logInActivity)
            val url="http://13.235.250.119/v2/login/fetch_result"
            val params=JSONObject()
            params.put("mobile_number",inputPhone)
            params.put("password",inputPassword)

            if(CollectionManager().checkConnectivity(this@logInActivity)) {
                val jsonObjectRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, params, Response.Listener {
                        val data = it.getJSONObject("data")
                        if (data.getBoolean("success")) {

                            val dataFinal = data.getJSONObject("data")

                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                            sharedPreferences.edit()
                                .putString("user_id", dataFinal.getString("user_id")).apply()

                            sharedPreferences.edit().putString("name", dataFinal.getString("name"))
                                .apply()

                            sharedPreferences.edit()
                                .putString("email", dataFinal.getString("email")).apply()

                            sharedPreferences.edit()
                                .putString("mobile_number", dataFinal.getString("mobile_number"))
                                .apply()

                            sharedPreferences.edit()
                                .putString("address", dataFinal.getString("address")).apply()

                            val intent =
                                Intent(this@logInActivity, WelcomeSplashFoodActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }, Response.ErrorListener {

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
                val dailog = AlertDialog.Builder(this@logInActivity)
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

        signUp.setOnClickListener {
            Unit
            val intent= Intent(this@logInActivity,
                RegisterActivity()::class.java)
            startActivity(intent)
        }

        forgot.setOnClickListener {
            Unit
            val intent= Intent(this@logInActivity, ForgotActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if(sharedPreferences.getBoolean("isLoggedIn",false)){
            finish()
        }
        super.onBackPressed()
    }
}
