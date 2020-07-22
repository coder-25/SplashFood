package com.example.splashfood.activity.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.splashfood.R
import org.json.JSONObject

class ForgotNextActivity : AppCompatActivity() {

    var retPhone: String? = "100"
    lateinit var retPassword: EditText
    lateinit var retOtp: EditText
    lateinit var bttnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_next)

        setUpId()
        retPhone = intent.getStringExtra("phone_no")

        bttnSubmit.setOnClickListener {
            val password = retPassword.text.toString()
            val otp = retOtp.text.toString()

            val queue = Volley.newRequestQueue(this@ForgotNextActivity)
            val url = "http://13.235.250.119/v2/reset_password/fetch_result"
            val params = JSONObject()
            params.put("mobile_number", retPhone)
            params.put("password", password)
            params.put("otp", otp)
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, params,
                Response.Listener {
                    val data = it.getJSONObject("data")

                    if (data.getBoolean("success")) {
                        Toast.makeText(this@ForgotNextActivity, data.getString("successMessage"), Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@ForgotNextActivity, logInActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@ForgotNextActivity, "try again", Toast.LENGTH_SHORT)
                            .show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@ForgotNextActivity,
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


    }

    fun setUpId() {
        retPassword = findViewById(R.id.txtForPassword)
        retOtp = findViewById(R.id.txtForOTP)
        bttnSubmit = findViewById(R.id.bttnSubmit)
    }

    override fun onBackPressed() {
        val intent = Intent(this@ForgotNextActivity, logInActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}
