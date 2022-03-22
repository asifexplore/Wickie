package com.example.Wickie.features.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.Wickie.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.Utils.BiometricLibrary
import com.example.Wickie.features.home.MainActivity
import com.example.Wickie.hardware.FingerprintLibrary

class LoginActivity : BaseActivity() {

    private lateinit var binding : ActivityLoginBinding
    public lateinit var viewModel: LoginViewModel
    public lateinit var sharedPref : SharedPreferences
    public lateinit var editor : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences("biometric", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        //viewModel.setContext(new MutableLiveData<>(this))

        enableFingerprint()


        binding.buttonSignIn.setOnClickListener()
        {
            login(1)
        }

        binding.imageButtonFingerprintScan.setOnClickListener {
            //var supported
            val intent = Intent(this, MainActivity::class.java)
            val biometricLibrary = BiometricLibrary(this)
            if (biometricLibrary.useBiometric() == true) {
                Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show()
                login(2)
            }

        }
    }
    /*
    * Gets Username & Password. Observes for mutable live data from login function in view model class.
    * Success: Intent to HomeActivity
    * Failed: Display Error Message
    * */
    public fun login(choice : Int)
    {
        var username = ""
        var password = ""
        if (choice == 1) {
            username = binding.editTextEmail.text.toString()
            password = binding.editTextPassword.text.toString()
            editor.apply {
                putString("username", username)
                putString("password", password)
                putBoolean("supported", true)
                apply()

            }
        }

        else {
            username = sharedPref.getString("username", null).toString()
            password = sharedPref.getString("password", null).toString()

        }



        viewModel.login(username, password).observe(this, Observer {
            if (it.status == 2){
                // Intent to next screen
                Log.d("LoginActivity", it.message.toString())
                Log.d("LoginActivity", it.userDetail.user_email.toString())
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


//    fingerprint feature with (shared preferences function, not sure how to update)
    private fun enableFingerprint(){
        val sharedBoolean = sharedPref.getBoolean("supported", false)
        if(sharedBoolean) {
            var check = binding.imageButtonFingerprintScan.isVisible
            binding.imageButtonFingerprintScan.isVisible = !check
        }
        else {
            binding.imageButtonFingerprintScan.isVisible = false

        }

    }

    private fun biometricLogin() {
        /*
        biometricViewModel.biometricLogin().observe(this, Observer {
            if (it.status == 2){
                // Intent to next screen
                Log.d("LoginActivity", it.message.toString())
                Log.d("LoginActivitys", it.userDetail.user_email.toString())
                openActivity(MainActivity::class.java)
            }else{
                if (it.message == "NO DATA FOUND")
                {
                    Log.d("LoginActivity", it.status.toString())
                    Log.d("LoginActivity", it.message.toString())

                }
            }
        })

         */
    }

//    TODO fingerprint feature with button on other page
//    private fun enableFingerprint(){
//        var check = binding.imageButtonFingerprintScan.isVisible
//        binding.imageButtonFingerprintScan.isVisible = !check
//    }

}