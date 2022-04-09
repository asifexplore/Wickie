package com.example.Wickie.features.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.Wickie.R
import com.example.Wickie.features.login.LoginActivity
/*
*   SplashscreenActivity is the responsible for the loading animation to the LoginActivity
 */

class SplashScreenActivity:AppCompatActivity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        handler = Handler()
        handler.postDelayed( {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }
}