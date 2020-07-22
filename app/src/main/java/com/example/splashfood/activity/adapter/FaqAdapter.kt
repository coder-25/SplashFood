package com.example.splashfood.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.QuickContactBadge
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splashfood.R
import com.example.splashfood.activity.model.QuesAns

class FaqAdapter(val context: Context, val quesAnsList:List<QuesAns>) :RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.single_row_faq , parent, false)
        return FaqViewHolder(view)
    }

    override fun getItemCount(): Int {
       return quesAnsList.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
       val quesAns=quesAnsList[position]
        holder.txtQues.text= quesAns.ques
        holder.txtAns.text= quesAns.ans

    }

    class FaqViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtQues:TextView = view.findViewById(R.id.ques)
        val txtAns:TextView = view.findViewById(R.id.ans)
    }

}