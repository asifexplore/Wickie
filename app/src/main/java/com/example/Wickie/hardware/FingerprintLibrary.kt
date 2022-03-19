package com.example.Wickie.hardware


import android.app.Activity
import android.app.KeyguardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager

import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.from
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat


import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.Wickie.BaseActivity
import java.util.concurrent.Executor

class FingerprintLibrary (currentActivity: Activity, intent: Intent, view: View)
{
    var currentActivity: Activity;
    private lateinit var intent: Intent;

    private lateinit var view: View;
    private lateinit var executor: Executor;
    private lateinit var biometricPrompt: BiometricPrompt;
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    init {
        this.currentActivity = currentActivity
        this.executor = ContextCompat.getMainExecutor(currentActivity)
        this.intent = intent
        this.view = view
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


    fun useBiometric() {

        val authCallBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(currentActivity, "Fingerprint feature not available", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                currentActivity.startActivity(intent)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                //Log.d(TAG, errString as String)
                Toast.makeText(currentActivity, errString, Toast.LENGTH_SHORT).show()
            }
        }

        val activity: FragmentActivity = currentActivity as FragmentActivity
        biometricPrompt = BiometricPrompt(activity, executor,
            authCallBack)
        this.promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Required")
            .setSubtitle("Please place your finger below the scanner")
            .setNegativeButtonText("Cancel")
            .build()

        this.view.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }



}