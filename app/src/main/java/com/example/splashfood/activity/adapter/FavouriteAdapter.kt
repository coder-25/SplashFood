package com.example.splashfood.activity.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.splashfood.R
import com.example.splashfood.activity.activity.MenuDescriptionActivity
import com.example.splashfood.activity.database.RestaurantEntity
import com.example.splashfood.activity.database.dataBase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.single_fav_item.view.*

class FavouriteAdapter(val context: Context,private val restaurantList: List<RestaurantEntity>) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
       val view= LayoutInflater.from(parent.context).inflate(R.layout.single_fav_item, parent, false)  // used Grid layout
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val restaurant= restaurantList[position]

        holder.txtName.text= restaurant.resName
        holder.txtPrice.text= "Rs.${restaurant.resPrice}"
        holder.txtRating.text= restaurant.resRating
        Picasso.get().load(restaurant.resImage).into(holder.imgRes)

        /*holder.imgFav.setOnClickListener {
            if(Deletefavourite(context, restaurant).execute().get()){
                holder.imgFav.setImageResource(R.drawable.ic_fav)
                Toast.makeText(
                    context,
                    "Restaurant is removed to Favourites",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }*/
        holder.imgFav.setOnClickListener {
            if (Deletefavourite(context, restaurant, 1).execute().get()) {
                if (Deletefavourite(context, restaurant, 3).execute().get()) {
                    holder.imgFav.setImageResource(R.drawable.ic_fav)
                    Toast.makeText(
                        context,
                        "Restaurant is removed to Favourites",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(context, "Unexpected Error occurred", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Deletefavourite(context, restaurant, 2).execute().get()) {
                    holder.imgFav.setImageResource(R.drawable.ic_fav_fill)
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

        holder.llContent.setOnClickListener {
            val intent= Intent(context, MenuDescriptionActivity::class.java)
            intent.putExtra("Res_Name", restaurant.resName)
            intent.putExtra("Res_Id", restaurant.restaurantId.toString())
            context.startActivity(intent)
        }

    }

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.favNameRes)
        val txtPrice: TextView = view.findViewById(R.id.favPriceRes)
        val txtRating: TextView = view.findViewById(R.id.favRatingRes)
        val imgRes: ImageView = view.findViewById(R.id.favImageRes)
        val imgFav: ImageView = view.findViewById(R.id.imgFav)
        val llContent: LinearLayout =view.findViewById(R.id.llContentFav)
    }

    class Deletefavourite(val context: Context,val restaurantEntity: RestaurantEntity, val mode:Int): AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db= Room.databaseBuilder(context, dataBase::class.java,"restaurants-db").build()
           /* db.restaurantDao().DeleteRestaurant(restaurantEntity)
            db.close()
            return true*/
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