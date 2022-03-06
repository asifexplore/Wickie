package com.example.Wickie.features.claims

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.Wickie.BaseActivity
import com.example.Wickie.databinding.ActivityClaimsformBinding
import com.example.Wickie.databinding.ActivityMainBinding
import com.example.Wickie.databinding.ClaimsDetailsBinding

class ViewClaimsActivity : AppCompatActivity() {
    private lateinit var binding : ClaimsDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ClaimsDetailsBinding.inflate(layoutInflater)
        var title = intent.getStringExtra("title")
        var claimDate = intent.getStringExtra("claimDate")
        var amount = intent.getStringExtra("amount")
        var type = intent.getStringExtra("type")
        var status = intent.getStringExtra("status")

        binding.textViewTitle.text = title.toString()
        binding.textViewDate.text = claimDate.toString()
        binding.textViewAmount.text = "$"+amount.toString()
        binding.textViewType.text = type.toString()
        binding.textViewStatus.text = status.toString()

        setContentView(binding.root)
    }






}