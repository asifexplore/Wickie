package com.example.Wickie.features.login

import android.os.Bundle
import android.util.Log
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

        binding.buttonLogin.setOnClickListener()
        {
            login()
        }
    }
    /*
    * Gets Username & Password. Observes for mutable live data from login function in view model class.
    * Success: Intent to HomeActivity
    * Failed: Display Error Message
    * */
    private fun login()
    {
        val username = binding.editTextSignInEmail.text.toString()
        val password = binding.editTextPw.text.toString()
        viewModel.login(username, password).observe(this, Observer {
            if (it.status == 2){
                // Intent to next screen
                show("Success")
                Log.d("LoginActivity", it.message.toString())
            }else{
                if (it.message.equals("NO DATA FOUND"))
                {
                    show("Failed")
                    Log.d("LoginActivity", it.status.toString())
                    Log.d("LoginActivity", it.message.toString())
                }
            }
        })
    }
}