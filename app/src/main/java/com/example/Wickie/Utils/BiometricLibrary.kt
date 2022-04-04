package com.example.Wickie.Utils


import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.Wickie.BaseActivity
import java.util.concurrent.Executor


class BiometricLibrary (currentActivity: BaseActivity, authCallBack : BiometricPrompt.AuthenticationCallback)
{
    private var currentActivity: BaseActivity = currentActivity;
    private var executor: Executor = ContextCompat.getMainExecutor(currentActivity);
    private lateinit var biometricPrompt: BiometricPrompt;
    private lateinit var promptInfo: BiometricPrompt.PromptInfo;
    private var authCallBack: BiometricPrompt.AuthenticationCallback = authCallBack;


    /*
    * checks if device has biometric function
    * checks permission for biometric
    * Success: Returns true
    * Fail: Returns false
     */
    fun hasBiometric(): Boolean {
        val keyguard = currentActivity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguard.isKeyguardSecure) {
            Toast.makeText(currentActivity,"Please enable fingerprint",Toast.LENGTH_SHORT).show()
            return false
        }

        if (ActivityCompat.checkSelfPermission(currentActivity, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {

        }

        return if (currentActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    } //hasBiometric

    /*
    * creates the biometric prompt dialog
    * authenticate using the callback function passed
    * during initialisation of instance
    * returns supported
    * Success: Open up the dialog and authenticate
     */
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