package com.example.Wickie.features.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.Wickie.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.Utils.BiometricLibrary
import com.example.Wickie.features.home.MainActivity


class LoginActivity : BaseActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var biometricLibrary: BiometricLibrary

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(( this as BaseActivity).authRepository ,(this as BaseActivity).sharedPrefRepo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.fingerprintStatus.observe(this, Observer {
            if (it == true)
            {
                binding.imageButtonFingerprintScan.visibility = View.VISIBLE
            }
            else
            {
                binding.imageButtonFingerprintScan.visibility = View.GONE
            }
        })

        val authCallBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                binding.imageButtonFingerprintScan.visibility = View.INVISIBLE

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                binding.imageButtonFingerprintScan.visibility = View.VISIBLE
                login(2)

            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@LoginActivity, errString, Toast.LENGTH_SHORT).show()
                binding.imageButtonFingerprintScan.visibility = View.INVISIBLE
            }

        }

        biometricLibrary = BiometricLibrary(this, authCallBack)

        binding.buttonSignIn.setOnClickListener()
        {
            login(1)
        }

        binding.imageButtonFingerprintScan.setOnClickListener {
            //var supported
            val intent = Intent(this, MainActivity::class.java)
//            val biometricLibrary = BiometricLibrary(this,)
//            if (biometricLibrary.useBiometric()) {
//                Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show()
//                login(2)
//            }
            biometricLibrary.useBiometric()

            binding.buttonForgotPassword.setOnClickListener()
            {
                forgotPw()
            }
        }
        /*
    * Gets Username & Password. Observes for mutable live data from login function in view model class.
    * Success: Intent to HomeActivity
    * Failed: Display Error Message
    */
    }
    private fun login(choice: Int) {
        var username = ""
        var password = ""
        if (choice == 1) {
            username = binding.editTextEmail.text.toString()
            password = binding.editTextPassword.text.toString()
        } else {
            username = loginViewModel.getUsername()
            Log.d("LoginAct",username)
            Log.d("LoginAct",password)
            password = loginViewModel.getPassword()
        }

        loginViewModel.login(username, password).observe(this, Observer {
            if (it.status == 2) {
                // Intent to next screen
                Log.d("LoginActivity", it.message.toString())
                Log.d("LoginActivity", it.userDetail.user_email.toString())
                loginViewModel.setUsername(username)
                loginViewModel.setPassword(password)
                openActivityWithIntent(MainActivity::class.java, username)
            } else {
                if (it.message == "NO DATA FOUND") {
//                    show("Incorrect Username or Password, Please Try Again!")
                    Log.d("LoginActivity", it.status.toString())
                    Log.d("LoginActivity", it.message.toString())

                }
            }
        })
    }

    private fun forgotPw() {
        show("HR has been notified")
    }

    //    fingerprint feature with (shared preferences function, not sure how to update)
    private fun enableFingerprint() {
        if (biometricLibrary.hasBiometric()) {
            binding.imageButtonFingerprintScan.visibility = View.VISIBLE
        } else {
            binding.imageButtonFingerprintScan.visibility = View.INVISIBLE
        }

    }


}