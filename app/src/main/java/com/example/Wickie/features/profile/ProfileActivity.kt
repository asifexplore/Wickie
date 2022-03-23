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


class ProfileActivity : BaseActivity() {
    private lateinit var binding : ActivityProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(( this as BaseActivity).userRepository ,(this as BaseActivity).sharedPrefRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize view binding
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        }

    }
    private fun displayUser(){
        val username = this.intent.getStringExtra("username").toString()
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
//                binding.EnableFingerprint.isChecked = it.userDetail.fingerprint

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
