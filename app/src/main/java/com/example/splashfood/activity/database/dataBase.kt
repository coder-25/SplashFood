package com.example.splashfood.activity.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity:: class], version =1)

abstract class dataBase: RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao
}
