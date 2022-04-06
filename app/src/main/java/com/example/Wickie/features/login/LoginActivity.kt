package com.example.Wickie.features.login

import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.Utils.BiometricLibrary
import com.example.Wickie.databinding.ActivityLoginBinding
import com.example.Wickie.features.home.MainActivity
import com.example.Wickie.services.NetworkService
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.Wickie.Validation
import com.example.Wickie.features.home.HomeViewModel
/*
* LoginActivity will be the activity responsible for logging
* in the user to the Wickie Application
*
 */


class LoginActivity : BaseActivity() {

    private lateinit var viewModel: HomeViewModel
    //Call NetworkService Class
    private lateinit var  networkService: NetworkService
    private var Bound : Boolean = false
    private var check: Boolean = false
    private var runnable : Runnable? = null
    //Set Handler to call functions
    private val handler = Handler(Looper.getMainLooper())

    //Create connection, callback for service binding, will be passed to bindService()
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as NetworkService.LocalBinder
            networkService = binder.getService()
            Bound = true
        }

        override fun onServiceDisconnected(argument: ComponentName) {
            Bound = false
            Log.d("ServiceActivity: ", "Service Disconnected")
        }
    }
    private lateinit var binding : ActivityLoginBinding
    private lateinit var biometricLibrary: BiometricLibrary

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(( this as BaseActivity).authRepository ,(this as BaseActivity).sharedPrefRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.postDelayed(Runnable {
            check = networkService.checkForInternet(this)
            if (!check) {
                Toast.makeText(this, "Please connect to a network", Toast.LENGTH_LONG).show()
            }
            Log.d("Service Activity", "NetworkService: $check added")
            handler.postDelayed(runnable!!, 10000)
        }.also { runnable = it }, 10000)

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
        /*
        * Initialise the callback
        * Success: Login
        * Fail: Send toast message "Login Failed"
        * Error: Send toast message to indicate the error
         */
        val authCallBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                show("Login Failed")
                binding.imageButtonFingerprintScan.visibility = View.VISIBLE
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                binding.imageButtonFingerprintScan.visibility = View.VISIBLE
                login(2, "", "")
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                show(errString.toString())
                Toast.makeText(this@LoginActivity, errString, Toast.LENGTH_SHORT).show()
                binding.imageButtonFingerprintScan.visibility = View.VISIBLE
            }
        }

        //Initialise the BiometricLibrary
        biometricLibrary = BiometricLibrary(this, authCallBack)

        //login through manual key input
        binding.buttonSignIn.setOnClickListener()
        {
            // Close Keyboard Dynamically
            hideKeyboard(this)
            login(1, binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
        }

        //use the biometric feature to login
        binding.imageButtonFingerprintScan.setOnClickListener {
            biometricLibrary.useBiometric()
        }
        binding.buttonForgotPassword.setOnClickListener()
        {
            forgotPw()
        }

    } // Oncreate()
    /*
    * Gets Username & Password. Observes for mutable live data from login function in view model class.
    * Success: Intent to HomeActivity
    * Failed: Display Error Message
    */

    private fun login(choice: Int, usernameInput:String, passwordInput : String) {
        startLoadingDialogBox("Validating Credentials...")
        var username = ""
        var password = ""
        if (choice == 1) {
            username = binding.editTextEmail.text.toString()
            password = binding.editTextPassword.text.toString()
            val validation = Validation()
            var loginStatus = validation.validateLogin(binding.editTextEmail, binding.editTextPassword)

        } else {
            username = loginViewModel.getUsername()
            Log.d("LoginAct",username)
            Log.d("LoginAct",password)
            password = loginViewModel.getPassword()
        }
        loginViewModel.login(username, password).observe(this) {
            if (it.status == 2) {
                // Intent to next screen
                Log.d("LoginActivity", it.message)
                Log.d("LoginActivity", it.userDetail.user_email.toString())
                loginViewModel.setUsername(username)
                loginViewModel.setPassword(password)
                openActivity(MainActivity::class.java)
            } else {
                if (it.message == "NO DATA FOUND") {
                    show("Incorrect Username or Password, Please Try Again!")
                    Log.d("LoginActivitys", it.userDetail.user_email.toString())
                    closeLoadingDialogBox()
                } else {
                    if (it.message == "NO DATA FOUND") {
                        Log.d("LoginActivity", it.status.toString())
                        Log.d("LoginActivity", it.message)
                        closeLoadingDialogBox()

                    }
                }
            }
        }
    } // Login

    /*
    * display toast message
    * once user clicks "Forget Password"
     */
    private fun forgotPw() {
        show("HR has been notified")
    }

    /*
    * Checks if the device has fingerprint feature
    * Success: set visibility of imageButtonFingerprintScan to visible
    * Failed: set visibility of imageButtonFingerprintScan to invisible
    */
    private fun enableFingerprint() {
        if (biometricLibrary.hasBiometric()) {
            binding.imageButtonFingerprintScan.visibility = View.VISIBLE
        } else {
            binding.imageButtonFingerprintScan.visibility = View.INVISIBLE
        }
    }//enableFingerprint

    /*
    * starts the NetworkService onStart
     */
    override fun onStart(){
        super.onStart()
        Intent(this, NetworkService::class.java).also { intent -> bindService(intent,connection,
            Context.BIND_AUTO_CREATE)}
    }//onStart

    /*
    * unbinds the NetworkService onStop
     */
    override fun onStop(){
        super.onStop()
        unbindService(connection)
        Bound = false
    }//onStop

} // LoginActivity