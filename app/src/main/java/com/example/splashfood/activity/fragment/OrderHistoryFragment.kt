package com.example.splashfood.activity.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.example.splashfood.R
import com.example.splashfood.activity.adapter.HistoryAdapter
import com.example.splashfood.activity.model.HistoryOrder
import com.example.splashfood.activity.util.CollectionManager


class OrderHistoryFragment : Fragment() {


    lateinit var recyclerHistory: RecyclerView
    lateinit var layoutRecycler:RecyclerView.LayoutManager
    lateinit var historyAdapter:HistoryAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    val hisOrder= arrayListOf<HistoryOrder>()
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_order_history, container, false)

        progressLayout=view.findViewById(R.id.progressLayoutHistory)
        progressLayout.visibility=View.VISIBLE

        val id=sharedPreferences.getString("user_id","100")
        val url="http://13.235.250.119/v2/orders/fetch_result/$id"
        val queue= Volley.newRequestQueue(activity as Context)


        if(CollectionManager().checkConnectivity(activity as Context )) {
            val jsonObject = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    val data = it.getJSONObject("data")
                    if (data.getBoolean("success")) {
                        val dataLog = data.getJSONArray("data")
                        println(dataLog)
                        for (i in 0 until dataLog.length()) {
                            val dataObj = dataLog.getJSONObject(i)

                            val hisObj = HistoryOrder(
                                dataObj.getString("restaurant_name"),
                                dataObj.getString("order_placed_at"),
                                dataObj.getJSONArray("food_items")
                            )
                            hisOrder.add(hisObj)
                        }

                        progressLayout.visibility = View.GONE

                        recyclerHistory = view.findViewById(R.id.recyclerHistory)
                        layoutRecycler = LinearLayoutManager(activity as Context)
                        historyAdapter = HistoryAdapter(activity as Context, hisOrder)

                        recyclerHistory.adapter = historyAdapter
                        recyclerHistory.layoutManager = layoutRecycler

                        recyclerHistory.addItemDecoration(
                            DividerItemDecoration(
                                recyclerHistory.context,
                                (layoutRecycler as LinearLayoutManager).orientation
                            )
                        )

                    } else {
                        Toast.makeText(
                            activity as Context,
                            "An unexpected error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        activity as Context,
                        "An unexpected volley error occurred",
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
            queue.add(jsonObject)
        }
        else{
            val dailog = AlertDialog.Builder(activity as Context)
            dailog.setTitle("Error")
            dailog.setMessage("Error In Internet Connection Found")
            dailog.setPositiveButton("Open Settings"){text,listner->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)    /// expilicit intent to open phone functions out ogf the app
                startActivity(settingIntent)
                activity?.finish()
            }
            dailog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dailog.create().show()

        }

        return view
    }

    override fun onAttach(context: Context) {
        sharedPreferences=context.getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)
        super.onAttach(context)
    }

}
