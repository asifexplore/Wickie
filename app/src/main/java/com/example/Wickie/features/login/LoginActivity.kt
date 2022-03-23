package com.example.Wickie.features.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.Wickie.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.features.home.MainActivity
import com.example.Wickie.hardware.FingerprintLibrary
import com.example.Wickie.features.splashscreen.SplashScreenActivity

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
        binding.imageButtonFingerprintScan.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val fingerprintLibrary = FingerprintLibrary(this, intent, binding.imageButtonFingerprintScan, viewModel)
            fingerprintLibrary.useBiometric()
        }

        binding.buttonForgotPassword.setOnClickListener()
        {
            forgotPw()
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
                Log.d("LoginActivitys", it.userDetail.user_email.toString())
                openActivityWithIntent(MainActivity::class.java,username)
            }else{
                if (it.message == "NO DATA FOUND")
                {
                    Log.d("LoginActivity", it.status.toString())
                    Log.d("LoginActivity", it.message.toString())

                }
            }
        })
    }

    private fun forgotPw()
    {
        show("HR has been notified")
    }

}