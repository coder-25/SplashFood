package com.example.splashfood.activity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity (
    @PrimaryKey val restaurantId:Int,
    @ColumnInfo(name="restaurant_name") val resName:String,
    @ColumnInfo(name="restaurant_Price") val resPrice:String,
    @ColumnInfo(name="restaurant_Rating") val resRating:String,
    @ColumnInfo(name="restaurant_Image") val resImage:String
)
