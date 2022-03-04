package com.example.Wickie.features.home
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.Wickie.R
import com.example.Wickie.databinding.FragmentHomeBinding
import com.example.Wickie.features.profile.ProfileActivity
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.features.login.LoginViewModel
import androidx.lifecycle.Observer

/*
*  Home Fragment will be the Activity for the Home Menu Screen
*  using other activities.
*
* Functions Within:
* ==========================================================================
* Function Name: Recycler View
* Function Purpose: Display Banner
* Function Arguments: Class (Activity)
* Results:
*         Success: Goes to next page
*         Failed:
*---------------------------------------------------
*/


class HomeFragment:Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //Initiate ViewModel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //All Buttons on Home Fragment


        //Profile Button Activity
        binding.layoutProfile.setOnClickListener {
            val intent = Intent(this.activity,ProfileActivity::class.java)
            startActivity(intent)
        }

        //Attendance Button Activity
        binding.layoutAttendance.setOnClickListener {
            //TODO
        }

        //Submit Claims Button via Camera Activity
        binding.layoutClaims.setOnClickListener {
            //TODO
        }

        //Wickie(Chatbot) Button Activity
        binding.layoutWickie.setOnClickListener {
            //TODO
        }

        val root: View = binding.root
        showQuote()
        return root
    }

    //Function to show quotes each day
    private fun showQuote(){
        viewModel.showQuote().observe(this.viewLifecycleOwner, Observer {
            binding.TextQuote.text = it.quoteDetail.mon_quote.toString()
        })
    }
}
