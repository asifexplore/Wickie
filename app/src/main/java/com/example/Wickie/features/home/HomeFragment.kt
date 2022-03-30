package com.example.Wickie.features.home
import LocationUtils
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
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.example.Wickie.BaseActivity
import com.example.Wickie.hardware.CameraLibrary
import com.example.Wickie.Utils.NotificationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


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
    private lateinit var notificationUtils: NotificationUtils
    private var alertDialog: AlertDialog? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(( this.requireContext() as BaseActivity).quoteRepository,( this.requireContext() as BaseActivity).attendanceRepository ,(this.requireContext() as BaseActivity).sharedPrefRepo)
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }
        Log.d("HomeFrag", homeViewModel.getUsername())
        binding.textUsername.text = homeViewModel.getUsername()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

        notificationUtils = NotificationUtils(this)
        notificationUtils.createNotificationChannel()

        //Profile Button Activity
        binding.layoutProfile.setOnClickListener {
            val intent = Intent(this.activity,ProfileActivity::class.java)
            startActivity(intent)
        }

        homeViewModel.currStatus.observe(viewLifecycleOwner){
            if (it == true)
            {
                binding.attendanceStatus.text = "Checked-In!"
                binding.attendanceStatusImg.setImageResource(R.drawable.attendance_icon)
            }
            else
            {
                binding.attendanceStatus.text = "Checked-Out!"
                binding.attendanceStatusImg.setImageResource(R.drawable.attendance_out)
            }
        }

        LocationUtils.getInstance(this.requireContext())
        LocationUtils.getCurrLocation()
        binding.layoutAttendance.setOnClickListener {
            LocationUtils.getCurrLocation()
            LocationUtils.location.let {
                Log.d("HomeFrag123", it.value?.latitude.toString())
                it.value?.longitude?.let { it1 -> it.value?.latitude?.let { it2 ->
                    homeViewModel.addLocation(it1,
                        it2)
                } }
            }

            // Error
            when (homeViewModel.setAttendance()) {
                0 -> {
                    Log.d("HomeFragment","Still Loading")
//                    Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    // Not Right Place
                    Toast.makeText(context, "Please stay within your work premises.", Toast.LENGTH_SHORT).show()
                }
                2 ->{
                    // Logging Out
                    Toast.makeText(context, "Checked Out Successfully", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Right Location
                    Toast.makeText(context, "Checked In Successfully!", Toast.LENGTH_SHORT).show()
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
        homeViewModel.showQuote().observe(this.viewLifecycleOwner) {
            binding.TextQuote.text = it.quoteDetail.mon_quote.toString()
        }
    }

    private fun launchCustomDialog() {
        val customLayout = LayoutInflater.from(this.activity).inflate(R.layout.dialog_mood_layout, null)
        val happy: ImageView = customLayout.findViewById(R.id.ImageViewHappy)
        val tired: ImageView = customLayout.findViewById(R.id.ImageViewTired)

        val builder = AlertDialog.Builder(this.activity)
            .setView(customLayout)

        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog?.show()
        happy.setOnClickListener {
                binding.imageMoodie.setImageResource(R.drawable.slimeball_happy)
                notificationUtils.sendNotification(resources,"Happy",R.drawable.slimeball_happy)
                val notificationHappy = true
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("HAPPY",notificationHappy)
                alertDialog?.cancel()
                startActivity(intent)
            }
        tired.setOnClickListener {
                binding.imageMoodie.setImageResource(R.drawable.slimeball_tired)
                notificationUtils.sendNotification(resources,"Tired",R.drawable.slimeball_tired)
                val notificationTired = true
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("TIRED",notificationTired)
                alertDialog?.cancel()
                startActivity(intent)
        }
    }

    //Send Chat Message
    private fun sendMessage(search: EditText){
        search.setOnEditorActionListener(TextView.OnEditorActionListener{ _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val word = binding.chatMessageText.text.toString()
                val messageWickie = true
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("KEY",word)
                    .putExtra("WICKIE",messageWickie)
                startActivity(intent)

                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragLifeCycle", "onResume()")
        Log.d("HomeFragLifeCycle",homeViewModel.getAttendanceStatus().toString())
        Log.d("HomeFragLifeCycle", homeViewModel.currStatus.value.toString())
            if (homeViewModel.currStatus.value == true) {
                binding.attendanceStatus.text = "Checked-In!"
                binding.attendanceStatusImg.setImageResource(R.drawable.attendance_icon)
            } else {
                binding.attendanceStatus.text = "Checked-Out!"
                binding.attendanceStatusImg.setImageResource(R.drawable.attendance_out)
            }
    }
}
