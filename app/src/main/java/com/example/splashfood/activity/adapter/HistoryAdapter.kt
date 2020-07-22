package com.example.splashfood.activity.adapter

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splashfood.R
import com.example.splashfood.activity.database_menu.MenuEntities
import com.example.splashfood.activity.model.FoodItem
import com.example.splashfood.activity.model.HistoryOrder
import kotlinx.android.synthetic.main.single_row_history.view.*

class HistoryAdapter(val context: Context, val order: ArrayList<HistoryOrder>):RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.single_row_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return order.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val order_item= order[position]
        holder.txtResNameHistory.text=order_item.resName
        holder.txtResDateHistory.text=order_item.resDate
        FoodList(context, holder, order_item)
    }

    class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtResNameHistory:TextView=view.findViewById(R.id.ResNameHistory)
        val txtResDateHistory:TextView=view.findViewById(R.id.ResDateHistory)
        val recyclerFoofList:RecyclerView=view.findViewById(R.id.recyclerFoodHistory)
    }


    private fun FoodList( context: Context,  holder:HistoryViewHolder,  orderItem: HistoryOrder){
        val orderList= arrayListOf<FoodItem>()
        for(i in 0 until orderItem.foodList.length()){
            val foodObj=orderItem.foodList.getJSONObject(i)
            val orderObject=FoodItem(
                foodObj.getString("food_item_id"),
               foodObj.getString("name"),
              foodObj.getString("cost")

           )
            orderList.add(orderObject)
        }
        val layoutManager=LinearLayoutManager(context)
        val foodListAdapter=cartAdapter(context, orderList)
        holder.recyclerFoofList.adapter=foodListAdapter
        holder.recyclerFoofList.layoutManager=layoutManager
    }

}