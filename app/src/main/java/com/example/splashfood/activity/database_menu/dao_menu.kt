package com.example.splashfood.activity.database_menu

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
abstract interface dao_menu {
    @Delete
    fun DeleteMneu(menuEntities: MenuEntities)

    @Insert
    fun InsertMenu(menuEntities: MenuEntities)

    @Query("SELECT * FROM menu")
    fun getAllRestaurants(): List<MenuEntities>

    @Query("SELECT * FROM menu WHERE menuId = :resId ")
    fun getREstaurantById(resId: String): MenuEntities
}