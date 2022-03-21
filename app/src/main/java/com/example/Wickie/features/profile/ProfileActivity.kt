package com.example.Wickie.features.profile


import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.Wickie.BaseActivity
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.databinding.ActivityProfileBinding
import com.example.Wickie.features.login.LoginActivity

class ProfileActivity : BaseActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize view binding
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        displayUser()
        //Logout Button
        binding.LogoutLayout.setOnClickListener {
            openActivity(LoginActivity::class.java)
        }
        binding.EditProfileLayout.setOnClickListener {
            displayUser()
            val username = binding.textViewName.text.toString()
            openActivityWithIntent(EditProfileActivity::class.java,username)
        }
    }
    private fun displayUser(){
        val username = this.intent.getStringExtra("username").toString()
        viewModel.retrieve(username).observe(this, Observer {
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