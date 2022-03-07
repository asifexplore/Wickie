package com.example.Wickie.hardware

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt


import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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


    fun useBiometric() {

        val authCallBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                currentActivity.startActivity(intent)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
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