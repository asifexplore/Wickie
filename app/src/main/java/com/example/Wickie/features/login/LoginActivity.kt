package com.example.Wickie.features.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
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
    public lateinit var biometricLibrary : BiometricLibrary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences("biometric", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authCallBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                editor.putBoolean("supported", false)
                editor.commit()
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                binding.imageButtonFingerprintScan.visibility = View.INVISIBLE

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                editor.putBoolean("supported", true)
                editor.commit()
                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                binding.imageButtonFingerprintScan.visibility = View.VISIBLE
                login(2)

            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                //Log.d(TAG, errString as String)
                editor.putBoolean("supported", false)
                editor.commit()
                Toast.makeText(this@LoginActivity, errString, Toast.LENGTH_SHORT).show()
                binding.imageButtonFingerprintScan.visibility = View.INVISIBLE

            }

        }

        biometricLibrary = BiometricLibrary(this, authCallBack)

        //viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        //viewModel.setContext(new MutableLiveData<>(this))

        /*
        if (biometricLibrary.hasBiometric()) {
            binding.imageButtonFingerprintScan.visibility = View.VISIBLE
        }
        else {
            if (sharedPref.getBoolean("supported", false).toString().toBoolean()) {
                binding.imageButtonFingerprintScan.visibility = View.VISIBLE
            }
            else {
                binding.imageButtonFingerprintScan.visibility = View.INVISIBLE
            }
        }
    */

        if (sharedPref.getBoolean("supported",false)) {
            binding.imageButtonFingerprintScan.visibility = View.VISIBLE

        }
        else {
            binding.imageButtonFingerprintScan.visibility = View.INVISIBLE
        }


        binding.buttonSignIn.setOnClickListener()
        {
            login(1)
        }

        binding.imageButtonFingerprintScan.setOnClickListener {
            //var supported
            val intent = Intent(this, MainActivity::class.java)
            //val biometricLibrary = BiometricLibrary(this,)
//            if (biometricLibrary.useBiometric()) {
//                Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show()
//                login(2)
//            }
            biometricLibrary.useBiometric()

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
        if (biometricLibrary.hasBiometric()) {
            binding.imageButtonFingerprintScan.visibility = View.VISIBLE
        }
        else {
            binding.imageButtonFingerprintScan.visibility = View.INVISIBLE
        }
    /*
        val sharedBoolean = sharedPref.getBoolean("supported", false)
        if(sharedBoolean) {
            var check = binding.imageButtonFingerprintScan.isVisible
            binding.imageButtonFingerprintScan.visibility = View.VISIBLE
        }
        else {
            binding.imageButtonFingerprintScan.visibility = View.INVISIBLE

        }

     */

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