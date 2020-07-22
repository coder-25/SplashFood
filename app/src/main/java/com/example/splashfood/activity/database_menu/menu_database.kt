package com.example.splashfood.activity.database_menu

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MenuEntities::class], version = 1 )
abstract class menu_database:RoomDatabase(){

    abstract fun daoMenu():dao_menu
}