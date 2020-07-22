package com.example.splashfood.activity.activity

import android.content.Intent
import android.media.audiofx.BassBoost
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.splashfood.R
import com.example.splashfood.activity.util.CollectionManager
import org.json.JSONObject

class ForgotActivity : AppCompatActivity() {

    lateinit var regNum: EditText
    lateinit var regEmail: EditText
    lateinit var sendMail: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        title = "Forgot Password"

        setUpUI()

        sendMail.setOnClickListener {
            Unit
            val inputRegNum = regNum.text.toString()
            val inputRegEmail = regEmail.text.toString()

            val queue = Volley.newRequestQueue(this@ForgotActivity)
            val url = "http://13.235.250.119/v2/forgot _password/fetch_result"
            val param = JSONObject()
            param.put("mobile_number", inputRegNum)
            param.put("email", inputRegEmail)


            if(CollectionManager().checkConnectivity(this@ForgotActivity)) {
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, param,
                    Response.Listener {
                        val data = it.getJSONObject("data")
                        if (data.getBoolean("success")) {
                            val intent = Intent(this@ForgotActivity, ForgotNextActivity::class.java)
                            intent.putExtra("phone_No", inputRegNum)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@ForgotActivity, "try again", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@ForgotActivity,
                            "Volley error occurred",
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
            }
            else{
                val dailog = AlertDialog.Builder(this@ForgotActivity)
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

    }

    fun setUpUI() {
        regNum = findViewById(R.id.txtRegNum)
        regEmail = findViewById(R.id.txtRegEmail)
        sendMail = findViewById(R.id.bttnSend)
    }

}
