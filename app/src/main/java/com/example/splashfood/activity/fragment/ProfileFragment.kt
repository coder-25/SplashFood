package com.example.splashfood.activity.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.example.splashfood.R


class ProfileFragment : Fragment() {

    lateinit var txtName:TextView
    lateinit var txtMobile:TextView
    lateinit var txtEmail:TextView
    lateinit var txtAddress:TextView


    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)

        txtName=view.findViewById(R.id.proName)
        txtMobile=view.findViewById(R.id.proMobile)
        txtEmail=view.findViewById(R.id.proEmail)
        txtAddress=view.findViewById(R.id.proAddress)

       val name=sharedPreferences.getString("name","Sneha")
        val userId=sharedPreferences.getString("user_id","100")
        val email=sharedPreferences.getString("email","sneh@gmail.com")
        val phoneNo=sharedPreferences.getString("mobile_number","Sneha")
        val address=sharedPreferences.getString("address","Delhi")

        txtName.text=name
        txtAddress.text=address
        txtMobile.text=phoneNo
        txtEmail.text=email

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences=context.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
    }

}
