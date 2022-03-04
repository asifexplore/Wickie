package com.example.Wickie.features.home
import android.os.Bundle
import com.example.Wickie.BaseActivity
import com.example.Wickie.R
import com.example.Wickie.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize view binding on activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize Fragments for the Navigation Bar (Claims, Home , Settings)
        val homeFragment=HomeFragment()
        val claimFragment=ClaimFragment()
        val settingsFragment=SettingsFragment()
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
                R.id.settings->setCurrentFragment(settingsFragment)
            }
            true
        }
        //Set Home Button as Default Selected Item in the Home Menu Screen
        binding.bottomNavigationView.selectedItemId = R.id.home
    }
}
