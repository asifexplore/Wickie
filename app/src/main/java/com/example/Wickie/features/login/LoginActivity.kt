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
//        viewModel.loginStatus().observe(this, Observer {
//            if (it == true )
//            {
//                // Intent to MainActivity
//                // Toast Message
//                val toast = Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_LONG)
//                toast.show()
//            }
//            else
//            {
//                // Toast Message
//                val toast = Toast.makeText(applicationContext, "Error Logging In", Toast.LENGTH_LONG)
//                toast.show()
//            }
//        })


        binding.buttonLogin.setOnClickListener()
        {
            login()
        }
    }

    private fun login()
    {
        val username = binding.editTextSignInEmail.text.toString()
        val password = binding.editTextPw.text.toString()
        viewModel.login(username, password).observe(this, Observer {
            if (it.status == 1){
                // Intent to next screen
                show("Success")
            }else{
                show("Failed")
            }
        })
    }
}