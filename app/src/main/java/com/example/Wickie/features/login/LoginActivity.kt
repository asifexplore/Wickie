package com.example.Wickie.features.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.Wickie.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity

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
            viewModel.checkLogin(username,password)
            Log.d("LoginActivity","Button Pressed")
            Log.d("LoginActivity Status",viewModel.loginStatus().toString())
        }
    }
}