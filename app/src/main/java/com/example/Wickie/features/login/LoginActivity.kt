package com.example.Wickie.features.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.Wickie.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.features.home.MainActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.loginStatus().observe(this, Observer {
            if (it == true )
            {
                // Intent to MainActivity
                // Toast Message
                val toast = Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_LONG)
                toast.show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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
        }
    }
}