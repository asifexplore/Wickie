package com.example.Wickie.features.profile

import android.os.Bundle
import android.util.Log
import com.example.Wickie.BaseActivity
import com.example.Wickie.databinding.EditProfileBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class EditProfileActivity : BaseActivity() {
    private lateinit var binding: EditProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize view binding
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding.Update.setOnClickListener {
            //Update Function
            update()
            openActivity(ProfileActivity::class.java)
            //TODO Use Datastore preference to store value, else it will disappear
        }

        binding.Cancel.setOnClickListener {
            openActivity(ProfileActivity::class.java)
            //TODO Use Datastore preference to store value, else it will disappear
        }
        displayProfile()
    }

    fun update(){


    }

    //Current Data of Profile
    private fun displayProfile(){
        val username = this.intent.getStringExtra("username").toString()
        viewModel.retrieve(username).observe(this, Observer {
            if (it.status == 2){
                // Intent to next screen
                Log.d("EditProfileActivity", it.message.toString())
                Log.d("EditProfileActivity", it.userDetail.user_email.toString())
                binding.EditTextNewName.setText(username)
                binding.EditTextNewEmail.setText(it.userDetail.user_email.toString())
                binding.EditTextMobileNew.setText(it.userDetail.user_mobile.toString())
                binding.EditTextNewDOB.setText(it.userDetail.user_dob.toString())
                binding.EditTextAddressNew.setText(it.userDetail.user_address.toString())
                binding.EditTextNewDepartment.setText(it.userDetail.user_department.toString())
//                binding.EnableFingerprint.isChecked = it.userDetail.fingerprint

            }else{
                if (it.message == "NO DATA FOUND")
                {
                    Log.d("EditProfileActivity", it.status.toString())
                    Log.d("EditProfileActivity", it.message.toString())

                }
            }
        })
    }
}