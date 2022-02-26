package com.example.Wickie.features.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.Wickie.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.features.home.MainActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.buttonSignIn.setOnClickListener()
        {
            login()
        }
    }
    /*
    * Gets Username & Password. Observes for mutable live data from login function in view model class.
    * Success: Intent to HomeActivity
    * Failed: Display Error Message
    * */
    private fun login()
    {
        val username = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        viewModel.login(username, password).observe(this, Observer {
            if (it.status == 2){
                // Intent to next screen
                Log.d("LoginActivity", it.message.toString())
                openActivity(MainActivity::class.java)
            }else{
                if (it.message == "NO DATA FOUND")
                {
                    Log.d("LoginActivity", it.status.toString())
                    Log.d("LoginActivity", it.message.toString())
                }
            }
        })
    }

//    fingerprint feature with (shared preferences function, not sure how to update)
//    private fun enableFingerprint(){
//        val sharedBoolean = sharedPreferences.getBoolean("check_key")
//        if(sharedBoolean) {
//            var check = binding.imageButtonFingerprintScan.isVisible
//            binding.imageButtonFingerprintScan.isVisible = !check
//        }
//    }

//    TODO fingerprint feature with button on other page
//    private fun enableFingerprint(){
//        var check = binding.imageButtonFingerprintScan.isVisible
//        binding.imageButtonFingerprintScan.isVisible = !check
//    }

}