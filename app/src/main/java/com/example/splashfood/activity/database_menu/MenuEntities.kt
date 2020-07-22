package com.example.splashfood.activity.database_menu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class MenuEntities (
    @PrimaryKey val menuId:Int,
    @ColumnInfo(name="menu_name") val menuName:String,
    @ColumnInfo(name = "menu_rate") val menuRate: String
)