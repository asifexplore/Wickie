package com.example.Wickie.hardware


import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.Wickie.features.login.LoginActivity
import com.example.Wickie.features.login.LoginViewModel
import java.util.concurrent.Executor


class FingerprintLibrary (currentActivity: LoginActivity, intent: Intent, view: View, viewModel: LoginViewModel)
{
    var currentActivity: Activity;
    private lateinit var intent: Intent;

    private lateinit var view: View;
    private lateinit var executor: Executor;
    private lateinit var biometricPrompt: BiometricPrompt;
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo;
    private lateinit var viewModel: LoginViewModel;

    init {
        this.currentActivity = currentActivity
        this.executor = ContextCompat.getMainExecutor(currentActivity)
        this.intent = intent
        this.view = view
        this.viewModel = viewModel

    }

    private fun hasBiometric(): Boolean {
        val keyguard = currentActivity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguard.isKeyguardSecure) {

            //informStatus("Please enable fingerprint authentication in the settings")
            Toast.makeText(currentActivity,"Please enable fingerprint",Toast.LENGTH_SHORT).show()
            return false
        }


        if (ActivityCompat.checkSelfPermission(currentActivity, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            //informStatus("Please enable fingerprint authentication")
            //return false
        }

        return if (currentActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }


    fun useBiometric() : Boolean {
        var supported: Boolean = true

        val authCallBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                supported = false
                Toast.makeText(
                    currentActivity,
                    "Fingerprint feature not available",
                    Toast.LENGTH_SHORT
                ).show()

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                supported = true
                Toast.makeText(currentActivity, "Login Success", Toast.LENGTH_SHORT).show()
                //currentActivity.startActivity(intent)\
                //val supported : Boolean = true
                //intent.putExtra("supported", true)
                //Toast.makeText(currentActivity, result.toString(), Toast.LENGTH_LONG).show()
                //val intent1 : Intent = currentActivity.intent
                //val sharedPref = currentActivity.getSharedPreferences("biometric", Context.MODE_PRIVATE)
                //val username = sharedPref.getString("username", null).toString()
                //currentActivity.login
                //currentActivity.startActivity(intent.putExtra("username", username))
                /*
                viewModel.login("asif", "123").observe(currentActivity, Observer {
                    if (it.status == 2){
                        // Intent to next screen
                        Log.d("LoginActivity", it.message.toString())
                        Log.d("LoginActivity", it.userDetail.user_email.toString())
                        currentActivity.openActivityWithIntent(MainActivity::class.java,username)
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

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                //Log.d(TAG, errString as String)
                supported = false
                Toast.makeText(currentActivity, errString, Toast.LENGTH_SHORT).show()

            }

        }

        val activity: FragmentActivity = currentActivity as FragmentActivity
        biometricPrompt = BiometricPrompt(
            activity, executor,
            authCallBack
        )
        this.promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Required")
            .setSubtitle("Please place your finger below the scanner")
            .setNegativeButtonText("Cancel")
            .build()
        /*
        this.view.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
            //return supported
        }
        */
        biometricPrompt.authenticate(promptInfo)
        return supported


    }


}



