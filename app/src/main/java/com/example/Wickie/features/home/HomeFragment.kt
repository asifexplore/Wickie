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
import com.example.Wickie.databinding.ActivityLoginBinding
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*


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
    var dialogBuilder: AlertDialog.Builder? = null
    var alertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        var username = this.requireActivity().intent.getStringExtra("username")
        binding.textUsername.text = username
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

        //Mood Dialog Activity
        binding.layoutMood.setOnClickListener {
            //TODO
            launchCustomDialog()
        }

        sendMessage(binding.chatMessageText)

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

    private fun launchCustomDialog() {
        val customLayout = LayoutInflater.from(this.activity).inflate(R.layout.dialog_mood_layout, null)
        val happy: Button = customLayout.findViewById(R.id.btnHappy)
        val tired: Button = customLayout.findViewById(R.id.btnTired)

        val builder = AlertDialog.Builder(this.activity)
            .setView(customLayout)

        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog?.show()
        happy.setOnClickListener {
                binding.imageMoodie.setImageResource(R.drawable.slimeball_happy)
                alertDialog?.cancel()
            }
        tired.setOnClickListener {
                binding.imageMoodie.setImageResource(R.drawable.slimeball_tired)
                alertDialog?.cancel()
        }
    }

    //Send Chat Message
    private fun sendMessage(search: EditText){
        search.setOnEditorActionListener(TextView.OnEditorActionListener{ _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEND) {
                Toast.makeText(context, "Sent", Toast.LENGTH_SHORT).show()

                return@OnEditorActionListener true
            }
            false
        })
    }


}
