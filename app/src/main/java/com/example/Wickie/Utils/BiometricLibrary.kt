package com.example.Wickie.Utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.lifecycle.Observer
import com.example.Wickie.BaseActivity
import com.example.Wickie.features.home.MainActivity
import com.example.Wickie.features.login.LoginActivity
import com.example.Wickie.features.login.LoginViewModel
import java.util.concurrent.Executor


class BiometricLibrary (currentActivity: BaseActivity, authCallBack : BiometricPrompt.AuthenticationCallback)
{
    private lateinit var currentActivity: BaseActivity;
    private lateinit var executor: Executor;
    private lateinit var biometricPrompt: BiometricPrompt;
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo;
    private lateinit var authCallBack: BiometricPrompt.AuthenticationCallback;



    init {
        this.currentActivity = currentActivity
        this.executor = ContextCompat.getMainExecutor(currentActivity)
        this.authCallBack = authCallBack
    }

    public fun hasBiometric(): Boolean {
        val keyguard = currentActivity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguard.isKeyguardSecure) {
            //informStatus("Please enable fingerprint authentication in the settings")
            Toast.makeText(currentActivity,"Please enable fingerprint",Toast.LENGTH_SHORT).show()
            return false
            //editor.putBoolean("supported", false)
        }


        if (ActivityCompat.checkSelfPermission(currentActivity, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {

        }

        return if (currentActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }


    fun useBiometric() : Boolean {
        var supported: Boolean = true;

        if (hasBiometric()) {
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

            biometricPrompt.authenticate(promptInfo)
        }
        return supported

    }


}