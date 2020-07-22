package com.example.splashfood.activity.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.splashfood.R
import com.example.splashfood.activity.database_menu.MenuEntities
import com.example.splashfood.activity.database_menu.menu_database
import com.example.splashfood.activity.model.ResMenuDescription
import kotlinx.android.synthetic.main.single_row_menu_description.view.*

class menuDescriptionAdapter (val context:Context, val menuList: ArrayList<ResMenuDescription>, val proceedCart:Button):
    RecyclerView.Adapter<menuDescriptionAdapter.menudecreptionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menudecreptionViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.single_row_menu_description, parent,false)
        return menudecreptionViewHolder(view)
    }

    override fun getItemCount(): Int {
       return menuList.size
    }

    override fun onBindViewHolder(holder: menudecreptionViewHolder, position: Int) {
        val menu= menuList[position]
        holder.txtMenuName.text= menu.mealName
        holder.txtMenuPrice.text= "Rs.${menu.mealPrice}"
        holder.txtMenuSno.text= "${position+1}"

        val menuEntity=MenuEntities(
            menu.mealId.toInt(),
            menu.mealName,
            menu.mealPrice
        )


        holder.bttnAddOrRemove.setOnClickListener {

            if(! asyncTask(context, menuEntity, 4).execute().get()){
                if (asyncTask(context, menuEntity, 2).execute().get()) {
                    holder.bttnAddOrRemove.text = "REMOVE"
                    Toast.makeText(context, "menu is added to thr cart", Toast.LENGTH_SHORT)
                        .show()
                    proceedCart.visibility = View.VISIBLE
                }

            }
            else {
                if (asyncTask(context, menuEntity, 1).execute().get()) {
                    if (asyncTask(context, menuEntity, 3).execute().get()) {
                        holder.bttnAddOrRemove.text = "ADD"
                        Toast.makeText(context, "menu removed from the cart", Toast.LENGTH_SHORT)
                            .show()
                        if ( asyncTask(context, menuEntity, 4).execute().get()) {
                            proceedCart.visibility = View.GONE
                        }

                    }
                } else {
                    if (asyncTask(context, menuEntity, 2).execute().get()) {
                        holder.bttnAddOrRemove.text = "REMOVE"
                        Toast.makeText(context, "menu is added to thr cart", Toast.LENGTH_SHORT)
                            .show()
                        proceedCart.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    class menudecreptionViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtMenuName:TextView= view.findViewById(R.id.menuName)
        val txtMenuPrice:TextView= view.findViewById(R.id.menuPrice)
        val txtMenuSno:TextView= view.findViewById(R.id.sno)
        val bttnAddOrRemove:Button= view.findViewById(R.id.addOrRemove)
        //val bttnAddToCart:Button= view.findViewById(R.id.bttnAddCart)
    }


    class asyncTask(val context: Context, val menuEntity: MenuEntities, val mode:Int):
        AsyncTask<Void, Void, Boolean>(){

        /*
       mode 1-> check whether a book is add to fav db  or not

       mode 2-> if not inserting the book to the fav db

       mode 3-> to delete a book from the fav db

       mode 4 checking whether db is empty or not
       */

        override fun doInBackground(vararg params: Void?): Boolean {
            val db= Room.databaseBuilder(context , menu_database::class.java , "menu-db").build()

            when(mode){

                1 ->{
                    val check:MenuEntities? =db.daoMenu().getREstaurantById(menuEntity.menuId.toString() )
                    db.close()
                    return check !=null
                }

                2 ->{
                    db.daoMenu().InsertMenu(menuEntity)
                    db.close()
                    return true
                }

                3->{
                    db.daoMenu().DeleteMneu(menuEntity)
                    db.close()
                    return true
                }
                4 ->{
                    val obj:List<MenuEntities>?=db.daoMenu().getAllRestaurants()
                    db.close()
                    return obj!=null
                }

            }

            return false

        }
    }




}