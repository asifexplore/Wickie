package com.example.Wickie.features.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.Wickie.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModal: LoginViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModal = ViewModelProvider(this).get(LoginViewModal::class.java)
        viewModal.loginStatus().observe(this, Observer {
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

        binding.buttonLogin.setOnClickListener()
        {
            val username = binding.editTextSignInEmail.text.toString()
            val password = binding.editTextPw.text.toString()
            viewModal.checkLogin(username,password)
            Log.d("LoginActivity","Button Pressed")
            Log.d("LoginActivity Status",viewModal.loginStatus().toString())
        }
    }
}