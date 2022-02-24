package com.example.Wickie.features.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.Wickie.R
import com.example.Wickie.databinding.ActivityProfileBinding
import com.example.Wickie.databinding.ActivityLoginBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var binding2: ActivityLoginBinding
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize view binding
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        binding.switch2.setOnClickListener() {
            val check = binding.switch2.isChecked
            binding2.imageButtonFingerprintScan.isVisible = check
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putBoolean("check_key",check)
            editor.apply()
            editor.commit()
        }
    }
}