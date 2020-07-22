package com.example.splashfood.activity.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.splashfood.R
import com.example.splashfood.activity.adapter.AllRestaurantsAdapter
import com.example.splashfood.activity.model.Restaurant
import com.example.splashfood.activity.util.CollectionManager
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class AllRestaurentsFragment : Fragment() {

    lateinit var recyclerAllrestaurants: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: AllRestaurantsAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    var restaurantList = arrayListOf<Restaurant>()

    var ratingComparator= Comparator<Restaurant>{res1, res2->
        if(res1.resRating.compareTo(res2.resRating, true)==0){
            res1.resName.compareTo(res2.resName,true)
        }
        else{
            res1.resRating.compareTo(res2.resRating, true)

        }

    }
    var priceComparator= Comparator<Restaurant>{res1, res2->
        if(res1.resPrice.compareTo(res2.resPrice, true)==0){
            res1.resName.compareTo(res2.resName,true)
        }
        else{
            res1.resPrice.compareTo(res2.resPrice, true)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_restaurents, container, false)


        setHasOptionsMenu(true)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressbar)
        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if(CollectionManager().checkConnectivity(activity as Context )) {
            val jsonObject =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        progressLayout.visibility = View.GONE
                        val resJsonArray = data.getJSONArray("data")
                        for (i in 0 until resJsonArray.length()) {
                            val restaurantJsonObject = resJsonArray.getJSONObject(i)

                            val restaurant = Restaurant(
                                restaurantJsonObject.getString("id"),
                                restaurantJsonObject.getString("name"),
                                restaurantJsonObject.getString("cost_for_one"),
                                restaurantJsonObject.getString("rating"),
                                restaurantJsonObject.getString("image_url")
                            )
                            restaurantList.add(restaurant)
                        }

                        recyclerAllrestaurants = view.findViewById(R.id.recyclerLayout)
                        layoutManager = LinearLayoutManager(activity)
                        recyclerAdapter = AllRestaurantsAdapter(activity as Context, restaurantList)
                        recyclerAllrestaurants.adapter = recyclerAdapter
                        recyclerAllrestaurants.layoutManager = layoutManager

                    } else {
                        Toast.makeText(
                            activity as Context,
                            "unexpected error occured",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        activity as Context,
                        "unexpected volley occured",
                        Toast.LENGTH_SHORT
                    )
                        .show()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_all_restaurants, menu )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            R.id.resPriceSortIc -> {
                Collections.sort(restaurantList, priceComparator)
            }
            R.id.resPriceSortDc -> {
                Collections.sort(restaurantList, priceComparator)
                restaurantList.reverse()
            }
            R.id.resSortRating -> {
                Collections.sort(restaurantList, ratingComparator)
                restaurantList.reverse()
            }
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}
