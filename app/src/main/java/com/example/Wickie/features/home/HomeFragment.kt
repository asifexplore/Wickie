package com.example.Wickie.features.home
import LocationUtils
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.Wickie.hardware.CameraLibrary
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task


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
    var attendanceState: Boolean = false

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }

        var username = this.requireActivity().intent.getStringExtra("username")
        binding.textUsername.text = username
        //Initiate ViewModel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //All Buttons on Home Fragment

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

        //Profile Button Activity
        binding.layoutProfile.setOnClickListener {
            val intent = Intent(this.activity,ProfileActivity::class.java)
            startActivity(intent.putExtra("username",username))
        }

        viewModel.currStatus.observe(viewLifecycleOwner){
            if (it == true)
            {
                binding.attendanceStatus.text = "Checked-In!"
            }
            else
            {
                binding.attendanceStatus.text = "Checked-Out!"
            }
        }

        LocationUtils.getInstance(this.requireContext())
        LocationUtils.getCurrLocation()
        binding.layoutAttendance.setOnClickListener {
            var tst = viewModel.checkLocation()
            Log.d("HomeFragment",tst.toString())
            LocationUtils.getCurrLocation()
            LocationUtils.location.let {
                Log.d("HomeFrag123", it.value?.latitude.toString())
                it.value?.longitude?.let { it1 -> it.value?.latitude?.let { it2 ->
                    viewModel.addLocation(it1.toDouble(),
                        it2.toDouble())
                } }
            }

            val attendanceStatus = viewModel.checkLocation()
            // Error
            when (attendanceStatus) {
                0 -> {
                    Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    // Not Right Place
                    Toast.makeText(context, "Please stay within your work premises.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Right Location
                    Toast.makeText(context, "Successfully Logged In!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Submit Claims Button via Camera Activity
        binding.layoutClaims.setOnClickListener {
            //TODO
            val current = this.activity as Activity
            val camera = CameraLibrary(current, current.packageManager)
            camera.useCamera()
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
