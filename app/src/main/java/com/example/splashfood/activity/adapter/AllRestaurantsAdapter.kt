package com.example.splashfood.activity.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.splashfood.R
import com.example.splashfood.activity.activity.MenuDescriptionActivity
import com.example.splashfood.activity.database.RestaurantEntity
import com.example.splashfood.activity.database.dataBase
import com.example.splashfood.activity.model.Restaurant
import com.squareup.picasso.Picasso

class AllRestaurantsAdapter(val context: Context, private val restaurantList: List<Restaurant>) :
    RecyclerView.Adapter<AllRestaurantsAdapter.AllRestaurantsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRestaurantsViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_all_restaurants, parent, false)
        return AllRestaurantsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantList.size

    }

    override fun onBindViewHolder(
        holder: AllRestaurantsAdapter.AllRestaurantsViewHolder,
        position: Int
    ) {
        val restaurant = restaurantList[position]

        holder.txtRestaurantName.text = restaurant.resName
        holder.txtRestaurantPrice.text = "Rs.${restaurant.resPrice}/person"
        holder.txtRestaurantRating.text = restaurant.resRating
        Picasso.get().load(restaurant.resImage).into(holder.imgRestaurantImage)

        val restaurantEntity=RestaurantEntity(
            restaurant.id.toInt(),
            restaurant.resName,
            restaurant.resPrice,
            restaurant.resRating,
            restaurant.resImage
        )

        if (Retrievefavourite(context, restaurantEntity, 1).execute().get()) {
            holder.imgRestaurantfav.setImageResource(R.drawable.ic_fav_fill)
        }

        holder.imgRestaurantfav.setOnClickListener {

            if (Retrievefavourite(context, restaurantEntity, 1).execute().get()) {
                if (Retrievefavourite(context, restaurantEntity, 3).execute().get()) {
                    holder.imgRestaurantfav.setImageResource(R.drawable.ic_fav)
                    Toast.makeText(
                        context,
                        "Restaurant is removed to Favourites",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Unexpected Error occurred", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Retrievefavourite(context, restaurantEntity, 2).execute().get()) {
                    holder.imgRestaurantfav.setImageResource(R.drawable.ic_fav_fill)
                    Toast.makeText(
                        context,
                        "Restaurant is added to Favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "Unexpected Error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.allContent.setOnClickListener {
            val intent= Intent(context, MenuDescriptionActivity::class.java)
            intent.putExtra("Res_Name", restaurant.resName)
            intent.putExtra("Res_Id", restaurant.id)
            context.startActivity(intent)
        }

    }


    class AllRestaurantsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtRestaurantName: TextView = view.findViewById(R.id.txtrestaurantName)
        val txtRestaurantPrice: TextView = view.findViewById(R.id.txtrestaurantPrice)
        val txtRestaurantRating: TextView = view.findViewById(R.id.txtrestaurantRating)
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgrestaurantImage)
        val imgRestaurantfav: ImageView = view.findViewById(R.id.imgrestaurantFav)
        val allContent:LinearLayout= view.findViewById(R.id.allContents)

    }

    class Retrievefavourite(
        val context: Context,
       private val restaurantEntity: RestaurantEntity,
        private val mode: Int
    ) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {

            /*
        mode 1-> check whether a book is add to fav db  or not

        mode 2-> if not inserting the book to the fav db

        mode 3-> to delete a book from the fav db
        */

            val db = Room.databaseBuilder(context, dataBase::class.java, "restaurants-db").build()

            when (mode) {

                1 -> {
                    val restaurant: RestaurantEntity? = db.restaurantDao()
                        .getREstaurantById(restaurantEntity.restaurantId.toString())
                    db.close()
                    return restaurant != null
                }

                2 -> {
                    db.restaurantDao().InsertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.restaurantDao().DeleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }

            return false
        }

    }


}