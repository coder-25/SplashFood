package com.example.splashfood.activity.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.example.splashfood.R
import com.example.splashfood.activity.adapter.FavouriteAdapter
import com.example.splashfood.activity.database.RestaurantEntity
import com.example.splashfood.activity.database.dataBase


class FavouriteFragment : Fragment() {

    lateinit var recyclerLayout: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var favAdapter: FavouriteAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var noFav:RelativeLayout
     var dbRestaurantEntityList= listOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favourite, container, false)

        noFav=view.findViewById(R.id.noFav)
        progressLayout= view.findViewById(R.id.favProgressLayout)
        progressBar= view.findViewById(R.id.favProgressBar)
        progressLayout.visibility= View.VISIBLE

        dbRestaurantEntityList=Retrievefavourite(activity as Context).execute().get()

        recyclerLayout= view.findViewById(R.id.favRecyclerLayout)
        layoutManager= GridLayoutManager(activity as Context, 2)

        progressLayout.visibility= View.GONE
        if(dbRestaurantEntityList!=null && activity != null){
            favAdapter=FavouriteAdapter(activity as Context, dbRestaurantEntityList)
            recyclerLayout.adapter= favAdapter
            recyclerLayout.layoutManager= layoutManager
        }
        else{
            noFav.visibility=View.VISIBLE
        }


        return view
    }

    class Retrievefavourite(val context: Context): AsyncTask<Void, Void, List<RestaurantEntity> >(){
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
           val db= Room.databaseBuilder(context, dataBase::class.java,"restaurants-db").build()
            return db.restaurantDao().getAllRestaurants()
        }

    }
}
