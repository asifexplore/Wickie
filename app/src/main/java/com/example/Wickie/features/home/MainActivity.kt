package com.example.Wickie.features.home
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.databinding.ActivityMainBinding
import com.example.Wickie.features.claims.ClaimsFormActivity
import com.example.Wickie.services.NetworkService

class MainActivity : BaseActivity() {
    private val REQUEST_IMAGE_CAMERA = 142
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize view binding on activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomNavigationView.itemIconTintList = null
        setContentView(binding.root)

        //Initialize Fragments for the Navigation Bar (Claims, Home , Settings)
        val homeFragment=HomeFragment()
        val claimFragment=ClaimFragment()
        val wickieFragment=WickieFragment()
        setCurrentFragment(homeFragment)

        //If Claims has been created, redirect back to claims fragment (logic to check)
        val check = intent.getBooleanExtra("exist",false)
        if(check){
            setCurrentFragment(claimFragment)
        }

        //Else Display Home Menu Screen with navigation bar
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFragment)
                R.id.claim->setCurrentFragment(claimFragment)
                R.id.wickie->setCurrentFragment(wickieFragment)
            }
            true
        }
        //Set Home Button as Default Selected Item in the Home Menu Screen
        binding.bottomNavigationView.selectedItemId = R.id.home
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {

            //binding.imageCamera.setImageBitmap(data.extras?.get("data") as Bitmap)
            val intent = Intent(this, ClaimsFormActivity::class.java)
            //intent.putExtra()
            val bitMapImage = data.extras?.get("data") as Bitmap
            intent.putExtra("BitmapImage", bitMapImage)
            startActivity(intent)
        }


    }
}
