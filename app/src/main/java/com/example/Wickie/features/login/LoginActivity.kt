package com.example.Wickie.features.login

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.Wickie.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.features.claims.ClaimsFormActivity
import com.example.Wickie.features.home.MainActivity


class LoginActivity : BaseActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    informStatus("Authentication error: $errString")

                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    informStatus("Valid fingerprint")
                    startActivity(Intent(this@LoginActivity, ClaimsFormActivity::class.java))
                }
            }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textForgetPassword.setOnClickListener {
            Toast.makeText(this,"Password Change sent to Admin", Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(this,"Welcome", Toast.LENGTH_SHORT).show()

        checkBiometricSupport()


        binding.imageButtonFingerprintScan.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Title of Prompt")
                .setSubtitle("Authentication is required")
                .setDescription("This app uses fingerprint")
                .setNegativeButton("Cancel", this.mainExecutor, DialogInterface.OnClickListener() { dialog, which ->
                    informStatus("Authentication cancelled")
                }).build()
            biometricPrompt.authenticate(getCancellationSignal(),mainExecutor,authenticationCallback)
        }

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.loginStatus().observe(this, Observer {
            if (it == true )
            {
                // Intent to MainActivity
                // Toast Message
                val toast = Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_LONG)
                toast.show()
            }
            else
            {
                // Toast Message
                val toast = Toast.makeText(applicationContext, "Error Logging In", Toast.LENGTH_LONG)
                toast.show()
            }
        })

        binding.buttonSignIn.setOnClickListener()
        {
            val username = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            viewModel.checkLogin(username,password)
            Log.d("LoginActivity","Button Pressed")
            Log.d("LoginActivity Status",viewModel.loginStatus().toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun informStatus(status: String) {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            informStatus("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            informStatus("Fingerprint authentication not enabled in settings")
            return false
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            informStatus("Fingerprint authentication is not enabled")
            return false
        }

        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }
}