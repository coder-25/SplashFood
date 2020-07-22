package com.example.splashfood.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splashfood.R
import com.example.splashfood.activity.database_menu.MenuEntities
import com.example.splashfood.activity.model.FoodItem
import kotlinx.android.synthetic.main.single_row_cart.view.*

class cartAdapter(val context:Context, val cartItem:ArrayList<FoodItem>): RecyclerView.Adapter<cartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.single_row_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
      return cartItem.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item= cartItem[position]
        holder.txtCartMenu.text=item.menuName
        holder.txtCartPrice.text="Rs."+ item.menuRate
    }
    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtCartMenu: TextView = view.findViewById(R.id.cartMenuNmae)
        val txtCartPrice:TextView=view.findViewById(R.id.cartMenuPrice)
    }


}