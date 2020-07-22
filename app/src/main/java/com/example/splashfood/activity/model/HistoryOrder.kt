package com.example.splashfood.activity.model

import org.json.JSONArray

data class HistoryOrder (
    val resName:String,
    val resDate:String,
    val foodList:JSONArray
    //val foodName:List<String>,
    //val foodPrice:ArrayList<String>,
    //val foodid:ArrayList<String>
)