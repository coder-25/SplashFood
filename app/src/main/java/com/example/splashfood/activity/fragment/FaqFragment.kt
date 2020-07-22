package com.example.splashfood.activity.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.splashfood.R
import com.example.splashfood.activity.adapter.FaqAdapter
import com.example.splashfood.activity.model.QuesAns


class FaqFragment : Fragment() {

    lateinit var recyclerFaq:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var faqAdapter: FaqAdapter

    /*private val quesAnsList= listOf<QuesAns>(
       QuesAns("Q1. What is a dial meal repaid account mode of payment ?", "A1. You can Fill-up a particular amount  NO minimum amount required… but, we recommend a minimum amount of INR. 500 and incremental slabs of INR. 500.\n" +
               "This Account Fill-up (initially) or Top-up (subsequently) can be done by CASH, ONLINE PAYMENT or CARD.\n" +
               "This option allows you the flexibility to order when you are on the move or to GIFT a Meal or to Order for someone else (family or friends), while you are on the move.")

       )*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_faq, container, false)

         val quesAnsList= listOf<QuesAns>(
            QuesAns(getString(R.string.q1), getString(R.string.a1) ),
            QuesAns(getString(R.string.q2), getString(R.string.a2) ),
            QuesAns(getString(R.string.q3), getString(R.string.a3) ),
            QuesAns(getString(R.string.q4), getString(R.string.a4) ),
            QuesAns(getString(R.string.q5), getString(R.string.a5) ),
            QuesAns(getString(R.string.q6), getString(R.string.a6) ),
            QuesAns(getString(R.string.q7), getString(R.string.a7) )
        )

        recyclerFaq= view.findViewById(R.id.faqRecyclerLayout)
        layoutManager=LinearLayoutManager(activity)
        faqAdapter= FaqAdapter(activity as Context, quesAnsList)
        recyclerFaq.adapter=faqAdapter
        recyclerFaq.layoutManager=layoutManager

        recyclerFaq.addItemDecoration(
            DividerItemDecoration(
                recyclerFaq.context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )

        return view
    }

}
