package com.example.Wickie.features.profile


import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.Wickie.BaseActivity
import com.example.Wickie.databinding.ActivityProfileBinding
import com.example.Wickie.features.login.LoginActivity
import LocationUtils
import androidx.fragment.app.viewModels
import com.example.Wickie.R
import com.example.Wickie.features.home.HomeViewModel
import com.example.Wickie.features.home.HomeViewModelFactory

class ProfileActivity : BaseActivity() {
    private lateinit var binding : ActivityProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(( this as BaseActivity).userRepository ,(this as BaseActivity).sharedPrefRepo)
    }

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(( this as BaseActivity).quoteRepository,( this as BaseActivity).attendanceRepository ,(this as BaseActivity).sharedPrefRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize view binding
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain Attendance Status
        homeViewModel.currStatus.observe(this){
            if (it == true)
            {
                binding.textViewSettings.text = "Checked-In!"
                binding.imageViewCheckIn.setImageResource(R.drawable.check_in)
            }
            else
            {
                binding.textViewSettings.text = "Checked-Out!"
                binding.imageViewCheckIn.setImageResource(R.drawable.checkoutprofile)
            }
        }


        // To initialise LocationUtils if instance was not created
        LocationUtils.getInstance(this)
        // Getting Current Location
        LocationUtils.getCurrLocation()

        displayUser()
        //Logout Button
        binding.LogoutLayout.setOnClickListener {
            // Destroy Shared Pref
            profileViewModel.logout()
            openActivity(LoginActivity::class.java)
        }
        binding.EditProfileLayout.setOnClickListener {
            show("Coming Soon")
        }

        binding.EnableFingerprint.setOnCheckedChangeListener { _, isChecked ->
            profileViewModel.setFingerPrintStatus(isChecked)
            if (isChecked)
            {
                show("Fingerprint Enabled")
            }else
            {
                show("Fingerprint Disabled")
            }
        }

        binding.imageViewCheckIn.setOnClickListener()
        {
            LocationUtils.getCurrLocation()
            LocationUtils.location.let {
                it.value?.longitude?.let { it1 -> it.value?.latitude?.let { it2 ->
                    homeViewModel.addLocation(it1.toDouble(),
                        it2.toDouble())
                } }
            }

            val attendanceStatus = homeViewModel.setAttendance()
            // Error
            when (attendanceStatus) {
                0 -> {
                    Log.d("HomeFragment","Still Loading")
//                    Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    // Not Right Place
                    Toast.makeText(this, "Please stay within your work premises.", Toast.LENGTH_SHORT).show()
                }
                2 ->{
                    // Logging Out
                    Toast.makeText(this, "Checked Out Successfully", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Right Location
                    Toast.makeText(this, "Checked In Successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun displayUser(){
        val username = profileViewModel.getUsername()
        profileViewModel.retrieve(username).observe(this, Observer {
            if (it.status == 2){
                // Intent to next screen
                Log.d("ProfileActivity", it.message)
                Log.d("ProfileActivity", it.userDetail.user_email.toString())
                binding.textViewName.text = username
                binding.textViewEmail.text = it.userDetail.user_email.toString()
                binding.textViewPositionValue.text = it.userDetail.user_position.toString()
                binding.textViewNumber.text = it.userDetail.user_mobile.toString()
                binding.textViewDOBValue.text = it.userDetail.user_dob.toString()
                binding.textViewAddressValue.text = it.userDetail.user_address.toString()
                binding.textViewDepartmentValue.text = it.userDetail.user_department.toString()

            }else{
                if (it.message == "NO DATA FOUND")
                {
                    Log.d("ProfileActivity", it.status.toString())
                    Log.d("ProfileActivity", it.message)
                }
            }
        })
    }

}
